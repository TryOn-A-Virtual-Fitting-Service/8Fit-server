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
import com.example.webapplicationserver.entity.User;
import com.example.webapplicationserver.enums.Category;
import com.example.webapplicationserver.enums.SuperType;
import com.example.webapplicationserver.repository.ClothRepository;
import com.example.webapplicationserver.repository.FittingRepository;
import com.example.webapplicationserver.repository.UserRepository;
import com.example.webapplicationserver.utils.ImageProcessUtils;
import com.example.webapplicationserver.utils.S3Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class FittingService {
    // repository layer
    private final UserRepository userRepository;
    private final FittingRepository fittingRepository;
    private final ClothRepository clothRepository;

    // utilize
    private final S3Utils s3Utils;
    private final ImageProcessUtils imageProcessUtils;

    // webClient
    private final WebClient webClient;

    @Value("${server-uri.fitting}")
    private String fittingServerUri;

    @Value("${server-uri.image-classification-worker}")
    private String imageClassificationWorkerUri;


    public ResponseFittingResultDto tryOnCloth(String deviceId, MultipartFile modelImage, MultipartFile clothImage) {
        // get user
        User user = userRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorStatus.USER_NOT_FOUND));

        Category clothCategory = predictClothImage(clothImage);
        if (!isFittingAvailable(clothCategory)) {
            throw new FittingExceptionHandler(ErrorStatus.UNSUPPORTED_CATEGORY);
        }

        // get fitting result from external server
        byte[] resultImage = postToFittingServerAndGetResult(modelImage, clothImage);

        // Apply Remove Background API to the result image
        byte[] backgroundRemovedResultImage = imageProcessUtils.removeBackground(resultImage);


        // upload fitting result image and Cloth image
        String clothImageUrl = s3Utils.uploadImage(clothImage);
        String resultImageUrl = s3Utils.uploadImage(backgroundRemovedResultImage);

        // save Cloth entity
        Cloth cloth = ClothConverter.toEntity(clothImageUrl, clothCategory);
        clothRepository.save(cloth);

        // save Fitting entity
        Fitting fitting = FittingConverter.toEntity(resultImageUrl, user, cloth);
        fittingRepository.save(fitting);

        return FittingConverter.toResponseFittingResultDto(fitting);

    }

    public byte[] postToFittingServerAndGetResult(MultipartFile modelImage, MultipartFile clothImage) {
        String fileName = uploadImagesAndGetUrlFromFittingServer(modelImage, clothImage);
        String imageUrl = fittingServerUri + "/static/" + fileName;
        return downloadImageFromUrl(imageUrl);
    }

    private String uploadImagesAndGetUrlFromFittingServer(MultipartFile modelImage, MultipartFile clothImage) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("model", modelImage.getResource());
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

        } catch (WebClientException e) {
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
