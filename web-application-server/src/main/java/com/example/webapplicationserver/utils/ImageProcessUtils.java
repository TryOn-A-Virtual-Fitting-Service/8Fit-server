package com.example.webapplicationserver.utils;

import com.example.webapplicationserver.apiPayload.code.status.ErrorStatus;
import com.example.webapplicationserver.apiPayload.exception.handler.FittingExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@RequiredArgsConstructor
@Component
public class ImageProcessUtils {

    private final WebClient webClient;

    @Value("${server-uri.image-classification-worker}")
    private String imageClassificationWorkerUri;


    public byte[] removeBackground(byte[] imageBytes) {
        String targetUri = imageClassificationWorkerUri + "/remove-background";
        ByteArrayResource resource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return "image.png";
            }
        };

        try {
            byte[] responseImage = webClient.post()
                    .uri(targetUri)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData("file", resource))
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();

            if (responseImage == null) {
                throw new FittingExceptionHandler(ErrorStatus.REMOVE_REMOVAL_ACTION_ERROR);
            }
            return responseImage;

        } catch (WebClientException e) {
            throw new FittingExceptionHandler(ErrorStatus.REMOVE_REMOVAL_SERVER_ERROR);
        }
    }
}
