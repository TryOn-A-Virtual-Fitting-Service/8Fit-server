package com.example.webapplicationserver.service;

import com.example.webapplicationserver.apiPayload.code.status.ErrorStatus;
import com.example.webapplicationserver.apiPayload.exception.handler.FittingExceptionHandler;
import com.example.webapplicationserver.apiPayload.exception.handler.UserExceptionHandler;
import com.example.webapplicationserver.converter.ClothConverter;
import com.example.webapplicationserver.converter.FittingConverter;
import com.example.webapplicationserver.dto.response.widget.ResponseFittingResultDto;
import com.example.webapplicationserver.entity.Cloth;
import com.example.webapplicationserver.entity.Fitting;
import com.example.webapplicationserver.entity.User;
import com.example.webapplicationserver.repository.ClothRepository;
import com.example.webapplicationserver.repository.FittingRepository;
import com.example.webapplicationserver.repository.UserRepository;
import com.example.webapplicationserver.utils.S3Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;


@Service
@RequiredArgsConstructor
public class FittingService {
    // repository layer
    private final UserRepository userRepository;
    private final FittingRepository fittingRepository;
    private final ClothRepository clothRepository;

    // utilize S3
    private final S3Utils s3Utils;

    // webClient Bean
    private final WebClient webClient;
    @Value("${server-uri.fitting}")
    private String fittingServerUri;


    public ResponseFittingResultDto tryOnCloth(String deviceId, MultipartFile modelImage, MultipartFile clothImage) {
        // get user
        User user = userRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorStatus.USER_NOT_FOUND));

        // TODO : image classification by image classification worker

        // get fitting result from external server
        byte[] resultImage = postToFittingServerAndGetResult(modelImage, clothImage);

        // upload fitting result image and Cloth image
        String clothImageUrl = s3Utils.uploadImage(clothImage);
        String resultImageUrl = s3Utils.uploadImage(resultImage);

        // save Cloth entity
        Cloth cloth = ClothConverter.toEntity(clothImageUrl);
        clothRepository.save(cloth);

        // save Fitting entity
        Fitting fitting = FittingConverter.toEntity(resultImageUrl, user, cloth);
        fittingRepository.save(fitting);

        return FittingConverter.toResponseFittingResultDto(fitting);

    }

    public byte[] postToFittingServerAndGetResult(MultipartFile modelImage, MultipartFile clothImage) {
        String imageUrl = uploadImagesAndGetUrl(modelImage, clothImage);
        return downloadImageFromUrl(imageUrl);
    }

    private String uploadImagesAndGetUrl(MultipartFile modelImage, MultipartFile clothImage) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("model", modelImage.getResource());
            builder.part("cloth", clothImage.getResource());

            return webClient.post()
                    .uri(fittingServerUri)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientException e) {
            throw new FittingExceptionHandler(ErrorStatus.FITTING_POST_ERROR);
        }
    }

    private byte[] downloadImageFromUrl(String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.isEmpty()) {
                throw new FittingExceptionHandler(ErrorStatus.FITTING_GET_ERROR);
            }

            return webClient.get()
                    .uri(imageUrl)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();

        } catch (WebClientException e) {
            throw new FittingExceptionHandler(ErrorStatus.FITTING_GET_ERROR);
        }
    }
}
