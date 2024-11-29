package com.example.webapplicationserver.service;

import com.example.webapplicationserver.apiPayload.code.status.ErrorStatus;
import com.example.webapplicationserver.apiPayload.exception.handler.FittingExceptionHandler;
import com.example.webapplicationserver.apiPayload.exception.handler.UserExceptionHandler;
import com.example.webapplicationserver.converter.ClothConverter;
import com.example.webapplicationserver.converter.FittingConverter;
import com.example.webapplicationserver.dto.external.InferenceResponseDto;
import com.example.webapplicationserver.dto.external.PredictResponseDto;
import com.example.webapplicationserver.dto.response.widget.ResponseFittingResultDto;
import com.example.webapplicationserver.entity.Cloth;
import com.example.webapplicationserver.entity.Fitting;
import com.example.webapplicationserver.entity.FittingModel;
import com.example.webapplicationserver.entity.User;
import com.example.webapplicationserver.enums.Category;
import com.example.webapplicationserver.enums.SuperType;
import com.example.webapplicationserver.repository.ClothRepository;
import com.example.webapplicationserver.repository.FittingModelRepository;
import com.example.webapplicationserver.repository.FittingRepository;
import com.example.webapplicationserver.repository.UserRepository;
import com.example.webapplicationserver.utils.ImageProcessUtils;
import com.example.webapplicationserver.utils.S3Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class FittingService {
    // repository layer
    private final UserRepository userRepository;
    private final FittingRepository fittingRepository;
    private final ClothRepository clothRepository;
    private final FittingModelRepository fittingModelRepository;

    // utilize
    private final S3Utils s3Utils;
    private final ImageProcessUtils imageProcessUtils;

    // webClient
    private final WebClient webClient;

    @Value("${server-uri.fitting}")
    private String fittingServerUri;

    @Value("${server-uri.image-classification-worker}")
    private String imageClassificationWorkerUri;


    @Transactional
    public ResponseFittingResultDto tryOnCloth(String deviceId, Long modelId, MultipartFile clothImage) {
        // 1. 유저 조회
        User user = getUser(deviceId);

        // 2. 카테고리 예측 및 지원 여부 확인
        Category clothCategory = predictClothImage(clothImage);
        validateFittingCategory(clothCategory);

        // 3. 모델 이미지 및 옷 이미지 링크 준비
        String clothImageUrl = s3Utils.uploadImage(clothImage);
        FittingModel fittingModel = fittingModelRepository.findById(modelId)
                .orElseThrow(() -> new FittingExceptionHandler(ErrorStatus.MODEL_NOT_FOUND));
        String fittingModelImageUrl = fittingModel.getImageUrl();

        // 4. 피팅 요청
        byte[] resultImage = postToFittingServerAndGetResult(clothImageUrl, fittingModelImageUrl);

        // 5. 결과 업로드
        resultImage = imageProcessUtils.removeBackground(resultImage);
        String resultImageUrl = s3Utils.uploadImage(resultImage);


        // 5. 데이터 저장
        Cloth cloth = saveCloth(clothImageUrl, clothCategory);
        Fitting fitting = saveFitting(resultImageUrl, fittingModel, cloth);
        updateFittingModelImage(fittingModel, resultImageUrl);

        // 6. 결과 반환
        return FittingConverter.toResponseFittingResultDto(fitting);
    }


    private User getUser(String deviceId) {
        return userRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorStatus.USER_NOT_FOUND));
    }

    private void validateFittingCategory(Category clothCategory) {
        if (!isFittingAvailable(clothCategory)) {
            throw new FittingExceptionHandler(ErrorStatus.UNSUPPORTED_CATEGORY);
        }
    }

    private FittingModel getFittingModel(Long modelId) {
        return fittingModelRepository.findById(modelId)
                .orElseThrow(() -> new FittingExceptionHandler(ErrorStatus.MODEL_NOT_FOUND));
    }

//    private byte[] processFittingImage(byte[] modelImage, MultipartFile clothImage) {
//        byte[] resultImage = postToFittingServerAndGetResult(modelImage, clothImage);
//        return imageProcessUtils.removeBackground(resultImage);
//    }

    private Cloth saveCloth(String clothImageUrl, Category clothCategory) {
        Cloth cloth = ClothConverter.toEntity(clothImageUrl, clothCategory);
        return clothRepository.save(cloth);
    }

    private Fitting saveFitting(String resultImageUrl, FittingModel fittingModel, Cloth cloth) {
        Fitting fitting = FittingConverter.toEntity(resultImageUrl, fittingModel, cloth);
        return fittingRepository.save(fitting);
    }

    private void updateFittingModelImage(FittingModel fittingModel, String resultImageUrl) {
        fittingModel.setImageUrl(resultImageUrl);
    }


    public byte[] postToFittingServerAndGetResult(String clothImageUrl, String fittingModelImageUrl) {
        String fileName = uploadImagesAndGetUrlFromFittingServer(clothImageUrl, fittingModelImageUrl);
        String imageUrl = fittingServerUri + "/static/" + fileName;
        return downloadImageFromUrl(imageUrl);
    }

    public String uploadImagesAndGetUrlFromFittingServer(String clothImageUrl, String fittingModelImageUrl) {
        try {
            String targetUri = UriComponentsBuilder.fromHttpUrl(fittingServerUri)
                    .path("/inference/")
                    .toUriString();

            // JSON payload 생성
            Map<String, String> requestPayload = new HashMap<>();
            requestPayload.put("clothing", clothImageUrl);
            requestPayload.put("model", fittingModelImageUrl);


            InferenceResponseDto response = webClient.post()
                    .uri(targetUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestPayload)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> clientResponse
                                    .bodyToMono(String.class)
                                    .map(errorMessage -> new IllegalArgumentException("Client error: " + errorMessage))
                    )
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            clientResponse -> clientResponse
                                    .bodyToMono(String.class)
                                    .map(errorMessage -> new IllegalStateException("Server error: " + errorMessage))
                    )
                    .bodyToMono(InferenceResponseDto.class)
                    .block();

            if (response == null) {
                throw new FittingExceptionHandler(ErrorStatus.FITTING_POST_ERROR);
            }

            return response.data();

        } catch (IllegalArgumentException e) {
            // 클라이언트 에러 처리
            System.err.println("Client error occurred: " + e.getMessage());
        } catch (IllegalStateException e) {
            // 서버 에러 처리
            System.err.println("Server error occurred: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Error Message: " + e.getMessage());
            e.printStackTrace();
            throw new FittingExceptionHandler(ErrorStatus.FITTING_WEBCLIENT_ERROR);
        }
        return null;
    }



    private void logRequestData(String uri, MultiValueMap<String, HttpEntity<?>> multipartData) {
        System.out.println("Request URI: " + uri);

        multipartData.forEach((key, value) -> {
            System.out.println("Request Part: " + key);
            value.forEach(part -> {
                if (part != null) {
                    HttpHeaders headers = part.getHeaders();
                    Object body = part.getBody();

                    System.out.println("  - Headers: " + headers);
                    if (body instanceof ByteArrayResource resource) {
                        System.out.println("  - Part Type: ByteArrayResource");
                        System.out.println("  - Filename: " + resource.getFilename());
                        System.out.println("  - Content Length: " + resource.contentLength());
                    } else {
                        System.out.println("  - Part Body: " + body.toString());
                    }
                }
            });
        });
    }

    private byte[] downloadImageFromUrl(String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.isEmpty()) {
                throw new FittingExceptionHandler(ErrorStatus.EMPTY_IMAGE_URL);
            }
            return webClient.get()
                    .uri(imageUrl)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();

        } catch (WebClientException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Failed to download image from URL: " + imageUrl + " - " + e.getMessage());
            throw new FittingExceptionHandler(ErrorStatus.FITTING_WEBCLIENT_ERROR);
        }
    }

    private Category predictClothImage(MultipartFile image) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", image.getResource());
            String targetUri = imageClassificationWorkerUri + "/predict";

            PredictResponseDto response = webClient.post()
                    .uri(targetUri)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(PredictResponseDto.class)
                    .block();

            if (response == null) {
                throw new FittingExceptionHandler(ErrorStatus.CLASSIFICATION_INDEX_ERROR);
            }
            return Category.valueOf(response.class_name());

        } catch (WebClientException e) {
            throw new FittingExceptionHandler(ErrorStatus.CLASSIFICATION_SEVER_ERROR);
        }
    }

    private boolean isFittingAvailable(Category category) {
        SuperType superType = category.getSuperType();
        return superType.equals(SuperType.TOP) || superType.equals(SuperType.BOTTOM);
    }




}
