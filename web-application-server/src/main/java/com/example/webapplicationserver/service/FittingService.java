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
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

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

        // 3. 모델 이미지 다운로드 및 피팅 결과 처리
        FittingModel fittingModel = getFittingModel(modelId);
        byte[] modelImage = downloadImageFromUrl(fittingModel.getImageUrl());
        byte[] fittingResultImage = processFittingImage(modelImage, clothImage);

        // 4. S3 업로드
        String clothImageUrl = s3Utils.uploadImage(clothImage);
        String resultImageUrl = s3Utils.uploadImage(fittingResultImage);

        // 5. 데이터 저장
        Cloth cloth = saveCloth(clothImageUrl, clothCategory);
        Fitting fitting = saveFitting(resultImageUrl, user, cloth);
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

    private byte[] processFittingImage(byte[] modelImage, MultipartFile clothImage) {
        byte[] resultImage = postToFittingServerAndGetResult(modelImage, clothImage);
        return imageProcessUtils.removeBackground(resultImage);
    }

    private Cloth saveCloth(String clothImageUrl, Category clothCategory) {
        Cloth cloth = ClothConverter.toEntity(clothImageUrl, clothCategory);
        return clothRepository.save(cloth);
    }

    private Fitting saveFitting(String resultImageUrl, User user, Cloth cloth) {
        Fitting fitting = FittingConverter.toEntity(resultImageUrl, user, cloth);
        return fittingRepository.save(fitting);
    }

    private void updateFittingModelImage(FittingModel fittingModel, String resultImageUrl) {
        fittingModel.setImageUrl(resultImageUrl);
    }


    public byte[] postToFittingServerAndGetResult(byte[] modelImage, MultipartFile clothImage) {
        String fileName = uploadImagesAndGetUrlFromFittingServer(modelImage, clothImage);
        String imageUrl = fittingServerUri + "/static/" + fileName;
        return downloadImageFromUrl(imageUrl);
    }

    private String uploadImagesAndGetUrlFromFittingServer(byte[] modelImage, MultipartFile clothImage) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("model", new ByteArrayResource(modelImage))
                    .header("Content-Disposition", "form-data; name=model; filename=model.jpg");
            builder.part("clothing", clothImage.getResource());
            String targetUri = fittingServerUri + "/inference/";

            InferenceResponseDto response = webClient.post()
                    .uri(targetUri)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(InferenceResponseDto.class)
                    .block();

            if (response == null) {
                throw new FittingExceptionHandler(ErrorStatus.FITTING_POST_ERROR);
            }
            return response.data();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new FittingExceptionHandler(ErrorStatus.FITTING_WEBCLIENT_ERROR);
        }
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
