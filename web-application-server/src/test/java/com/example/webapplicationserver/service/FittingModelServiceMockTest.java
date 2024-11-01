package com.example.webapplicationserver.service;

import com.example.webapplicationserver.apiPayload.code.status.ErrorStatus;
import com.example.webapplicationserver.apiPayload.exception.handler.S3ExceptionHandler;
import com.example.webapplicationserver.apiPayload.exception.handler.UserExceptionHandler;
import com.example.webapplicationserver.dto.response.widget.ResponseFittingModelDto;
import com.example.webapplicationserver.entity.FittingModel;
import com.example.webapplicationserver.entity.User;
import com.example.webapplicationserver.model.UserFixture;
import com.example.webapplicationserver.repository.FittingModelRepository;
import com.example.webapplicationserver.repository.UserRepository;
import com.example.webapplicationserver.utils.S3Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FittingModelServiceMockTest {
    private FittingModelRepository fittingModelRepository;
    private UserRepository userRepository;
    private S3Utils s3Utils;
    private FittingModelService fittingModelService;

    @BeforeEach
    public void setUp() {
        // repository initialization
        fittingModelRepository = mock(FittingModelRepository.class);
        userRepository = mock(UserRepository.class);
        s3Utils = mock(S3Utils.class);

        // service initialization
        fittingModelService = new FittingModelService(
                fittingModelRepository,
                userRepository,
                s3Utils
        );
    }

    @Test
    void uploadFittingModel_Success() {
        // Arrange
        String deviceId = "test-device-id";
        String expectedImageUrl = "https://s3.amazonaws.com/bucket/test-image.jpg";
        User user = UserFixture.createTestUserWithDeviceId(deviceId);
        MultipartFile image = mock(MultipartFile.class);

        when(image.isEmpty()).thenReturn(false);
        when(userRepository.findByDeviceId(deviceId)).thenReturn(Optional.of(user));
        when(s3Utils.uploadImage(image)).thenReturn(expectedImageUrl);

        // Act
        ResponseFittingModelDto response = fittingModelService.uploadFittingModel(deviceId, image);

        // Assert
        assertEquals(expectedImageUrl, response.modelUrl());
        verify(userRepository, times(1)).findByDeviceId(deviceId);
        verify(s3Utils, times(1)).uploadImage(image);
        verify(fittingModelRepository, times(1)).save(any(FittingModel.class));
    }

    @Test
    void uploadFittingModel_FileEmpty() {
        // Arrange
        String deviceId = "test-device-id";
        MultipartFile image = mock(MultipartFile.class);

        when(image.isEmpty()).thenReturn(true);

        // Act & Assert
        S3ExceptionHandler exception = assertThrows(S3ExceptionHandler.class, () ->
                fittingModelService.uploadFittingModel(deviceId, image)
        );
        assertEquals(ErrorStatus.FILE_EMPTY, exception.getBaseErrorCode());
        verify(userRepository, never()).findByDeviceId(anyString());
        verify(s3Utils, never()).uploadImage(any(MultipartFile.class));
        verify(fittingModelRepository, never()).save(any(FittingModel.class));
    }

    @Test
    void uploadFittingModel_UserNotFound() {
        // Arrange
        String deviceId = "non-existent-device-id";
        MultipartFile image = mock(MultipartFile.class);

        when(image.isEmpty()).thenReturn(false);
        when(userRepository.findByDeviceId(deviceId)).thenReturn(Optional.empty());

        // Act & Assert
        UserExceptionHandler exception = assertThrows(UserExceptionHandler.class, () ->
                fittingModelService.uploadFittingModel(deviceId, image)
        );
        assertEquals(ErrorStatus.USER_NOT_FOUND, exception.getBaseErrorCode());
        verify(userRepository, times(1)).findByDeviceId(deviceId);
        verify(s3Utils, never()).uploadImage(any(MultipartFile.class));
        verify(fittingModelRepository, never()).save(any(FittingModel.class));
    }

}
