package com.example.webapplicationserver.service;

import com.example.webapplicationserver.apiPayload.exception.handler.S3ExceptionHandler;
import com.example.webapplicationserver.apiPayload.exception.handler.UserExceptionHandler;
import com.example.webapplicationserver.entity.Cloth;
import com.example.webapplicationserver.entity.Fitting;
import com.example.webapplicationserver.entity.User;
import com.example.webapplicationserver.model.ClothFixture;
import com.example.webapplicationserver.model.FittingFixture;
import com.example.webapplicationserver.model.UserFixture;
import com.example.webapplicationserver.repository.ClothRepository;
import com.example.webapplicationserver.repository.FittingModelRepository;
import com.example.webapplicationserver.repository.FittingRepository;
import com.example.webapplicationserver.repository.UserRepository;
import com.example.webapplicationserver.utils.S3Utils;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FittingServiceMockTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClothRepository clothRepository;
    @Mock
    private FittingRepository fittingRepository;
    @Mock
    private S3Utils s3Utils;

    @InjectMocks
    private FittingService fittingService;

    private User testUser;
    private MultipartFile testImage;
    private String testDeviceId;
    private String clothImageUrl;
    private String resultImageUrl;
    private Cloth testCloth;
    private Fitting testFitting;

    @BeforeEach
    public void setUp() {
        // Fixture를 사용하여 기본 테스트 데이터 생성
        testDeviceId = "testDeviceId123";
        testUser = UserFixture.createTestUserWithDeviceId(testDeviceId);
        testImage = new MockMultipartFile("image", "test.jpg", "image/jpeg", "image data".getBytes());
        clothImageUrl = "https://s3-bucket-url/clothImage.jpg";
        resultImageUrl = "https://s3-bucket-url/resultImage.jpg";
        testCloth = ClothFixture.createTestCloth(clothImageUrl);
        testFitting = FittingFixture.createTestFitting(testUser, testCloth, resultImageUrl);
    }

    @Test
    public void testTryOnCloth() {
        // Arrange: Mock 객체의 행동 설정
        when(userRepository.findByDeviceId(testDeviceId)).thenReturn(Optional.of(testUser));
        when(s3Utils.uploadImage(testImage)).thenReturn(clothImageUrl, resultImageUrl);
        when(clothRepository.save(any(Cloth.class))).thenReturn(testCloth);
        when(fittingRepository.save(any(Fitting.class))).thenReturn(testFitting);


        // Act: 테스트 대상 메서드 실행
        fittingService.tryOnCloth(testDeviceId, testImage);

        // Assert: 예상되는 저장 동작을 검증
        verify(clothRepository).save(any(Cloth.class));
        verify(fittingRepository).save(any(Fitting.class));
        verify(s3Utils, times(2)).uploadImage(testImage);
    }

    @Test
    public void testTryOnCloth_FileEmpty() {
        // Arrange: 비어 있는 파일로 설정
        MultipartFile emptyImage = new MockMultipartFile("image", "", "image/jpeg", new byte[0]);

        // Act & Assert: 예외 발생을 검증
        assertThrows(S3ExceptionHandler.class, () -> fittingService.tryOnCloth(testDeviceId, emptyImage));
    }

    @Test
    public void testTryOnCloth_UserNotFound() {
        // Arrange: 존재하지 않는 userId 설정
        when(userRepository.findByDeviceId("invalidDeviceId")).thenReturn(Optional.empty());

        // Act & Assert: 예외 발생을 검증
        assertThrows(UserExceptionHandler.class, () -> fittingService.tryOnCloth("invalidDeviceId", testImage));
    }

}
