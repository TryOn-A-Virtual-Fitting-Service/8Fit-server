//package com.example.webapplicationserver.service;
//
//import com.example.webapplicationserver.apiPayload.code.status.ErrorStatus;
//import com.example.webapplicationserver.apiPayload.exception.handler.S3ExceptionHandler;
//import com.example.webapplicationserver.apiPayload.exception.handler.UserExceptionHandler;
//import com.example.webapplicationserver.dto.response.widget.ResponseFittingModelDto;
//import com.example.webapplicationserver.entity.FittingModel;
//import com.example.webapplicationserver.entity.User;
//import com.example.webapplicationserver.model.UserFixture;
//import com.example.webapplicationserver.repository.FittingModelRepository;
//import com.example.webapplicationserver.repository.UserRepository;
//import com.example.webapplicationserver.utils.S3Utils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class FittingModelServiceMockTest {
//    @Mock
//    private FittingModelRepository fittingModelRepository;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private S3Utils s3Utils;
//    @InjectMocks
//    private FittingModelService fittingModelService;
//
//    @Test
//    @DisplayName("Fitting Model 업로드 성공")
//    void uploadFittingModel_Success() {
//        // Arrange
//        String deviceId = "test-device-id";
//        String expectedImageUrl = "https://s3.amazonaws.com/bucket/test-image.jpg";
//        User user = UserFixture.createTestUserWithDeviceId(deviceId);
//        MultipartFile image = mock(MultipartFile.class);
//
//        when(image.isEmpty()).thenReturn(false);
//        when(userRepository.findByDeviceId(deviceId)).thenReturn(Optional.of(user));
//        when(s3Utils.uploadImage(image)).thenReturn(expectedImageUrl);
//
//        // Act
//        ResponseFittingModelDto response = fittingModelService.uploadFittingModel(deviceId, image);
//
//        // Assert
//        assertEquals(expectedImageUrl, response.modelUrl());
//        verify(userRepository, times(1)).findByDeviceId(deviceId);
//        verify(s3Utils, times(1)).uploadImage(image);
//        verify(fittingModelRepository, times(1)).save(any(FittingModel.class));
//    }
//
//    @Test
//    @DisplayName("Fitting Model 업로드 실패: 파일이 비어있을 때")
//    void uploadFittingModel_FileEmpty() {
//        // Arrange
//        String deviceId = "test-device-id";
//        MultipartFile image = mock(MultipartFile.class);
//
//        when(image.isEmpty()).thenReturn(true);
//
//        // Act & Assert
//        S3ExceptionHandler exception = assertThrows(S3ExceptionHandler.class, () ->
//                fittingModelService.uploadFittingModel(deviceId, image)
//        );
//        assertEquals(ErrorStatus.FILE_EMPTY, exception.getBaseErrorCode());
//        verify(userRepository, never()).findByDeviceId(anyString());
//        verify(s3Utils, never()).uploadImage(any(MultipartFile.class));
//        verify(fittingModelRepository, never()).save(any(FittingModel.class));
//    }
//
//    @Test
//    @DisplayName("Fitting Model 업로드 실패: 사용자를 찾을 수 없을 때")
//    void uploadFittingModel_UserNotFound() {
//        // Arrange
//        String deviceId = "non-existent-device-id";
//        MultipartFile image = mock(MultipartFile.class);
//
//        when(image.isEmpty()).thenReturn(false);
//        when(userRepository.findByDeviceId(deviceId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        UserExceptionHandler exception = assertThrows(UserExceptionHandler.class, () ->
//                fittingModelService.uploadFittingModel(deviceId, image)
//        );
//        assertEquals(ErrorStatus.USER_NOT_FOUND, exception.getBaseErrorCode());
//        verify(userRepository, times(1)).findByDeviceId(deviceId);
//        verify(s3Utils, never()).uploadImage(any(MultipartFile.class));
//        verify(fittingModelRepository, never()).save(any(FittingModel.class));
//    }
//
//}
