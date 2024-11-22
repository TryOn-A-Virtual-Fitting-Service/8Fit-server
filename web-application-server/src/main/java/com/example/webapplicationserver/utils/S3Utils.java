package com.example.webapplicationserver.utils;

import com.example.webapplicationserver.apiPayload.code.status.ErrorStatus;
import com.example.webapplicationserver.apiPayload.exception.handler.S3ExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Utils {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${server-uri.domain}")
    private String domain;



    public String uploadImage(MultipartFile image) {
        // set unique file name
        String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        // object to upload to S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(imageName)
                .contentType("image/png")
                .build();

        // upload
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(image.getBytes()));
        } catch (IOException e) {
            throw new S3ExceptionHandler(ErrorStatus.FILE_UPLOAD_ERROR);
        }
        log.info("File uploaded to S3: {}", imageName);

        return domain + "/" + imageName;
    }

    public String uploadImage(byte[] image) {
        // object to upload to S3
        String imageName = UUID.randomUUID() + ".png";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(imageName)
                .contentType("image/png")
                .build();

        // upload
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(image));
        } catch (Exception e) {
            throw new S3ExceptionHandler(ErrorStatus.FILE_UPLOAD_ERROR);
        }
        log.info("File uploaded to S3: {}", imageName);

        return domain + "/" + imageName;
    }
}
