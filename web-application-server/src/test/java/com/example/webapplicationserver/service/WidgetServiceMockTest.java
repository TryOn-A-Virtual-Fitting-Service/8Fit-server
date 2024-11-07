//package com.example.webapplicationserver.service;
//
//import com.example.webapplicationserver.dto.response.widget.ResponseWidgetDto;
//import com.example.webapplicationserver.entity.Fitting;
//import com.example.webapplicationserver.entity.User;
//import com.example.webapplicationserver.model.UserFixture;
//import com.example.webapplicationserver.repository.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class WidgetServiceMockTest {
//    @Mock
//    private UserRepository userRepository;
//    @InjectMocks
//    private WidgetService widgetService;
//
//    @Test
//    @DisplayName("기기가 존재할 때 기기 정보를 저장하지 않고 위젯 정보를 가져온다.")
//    void getWidgetInfoTest_UserExists() {
//        // Arrange
//        String deviceId = "existing-device-id";
//        User existingUser = UserFixture.createTestUserWithDeviceId(deviceId);
//
//        // Mocking userRepository to return an existing user
//        when(userRepository.findByDeviceId(deviceId)).thenReturn(Optional.of(existingUser));
//
//        // Act
//        ResponseWidgetDto response = widgetService.getWidgetInfo(deviceId);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(existingUser.getFittings().size(), response.models().size());
//        verify(userRepository, times(1)).findByDeviceId(deviceId);
//        verify(userRepository, never()).save(any(User.class));
//
//        // 검증: `models` 리스트에 있는 URL이 `Fitting` 객체의 URL과 일치하는지 확인
//        for (int i = 0; i < existingUser.getFittings().size(); i++) {
//            Fitting fitting = existingUser.getFittings().stream().toList().get(i);
//            ResponseWidgetDto.Model model = response.models().get(i);
//            assertEquals(fitting.getCloth().getImageUrl(), model.itemImageUrl());
//            assertEquals(fitting.getCloth().getImageUrl(), model.itemImageUrl());
//            assertEquals(fitting.getImageUrl(), model.modelImageUrl());
//        }
//    }
//
//    @Test
//    @DisplayName("기기가 존재하지 않을 때 DB에 기기 정보를 저장하고 위젯 정보를 가져온다.")
//    void getWidgetInfoTest_UserDoesNotExist() {
//        // Arrange
//        String deviceId = "non-existing-device-id";
//        User newUser = UserFixture.createTestUserWithDeviceId(deviceId);
//
//        // Mocking userRepository to return an empty optional
//        when(userRepository.findByDeviceId(deviceId)).thenReturn(Optional.empty());
//        when(userRepository.save(any(User.class))).thenReturn(newUser);
//
//        // Act
//        ResponseWidgetDto response = widgetService.getWidgetInfo(deviceId);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(newUser.getFittings().size(), response.models().size());
//        verify(userRepository, times(1)).findByDeviceId(deviceId);
//        verify(userRepository, times(1)).save(any(User.class));
//
//        // 검증: `models` 리스트의 URL이 `Fitting` 객체의 URL과 일치하는지 확인
//        for (int i = 0; i < newUser.getFittings().size(); i++) {
//            Fitting fitting = newUser.getFittings().stream().toList().get(i);
//            ResponseWidgetDto.Model model = response.models().get(i);
//            assertEquals(fitting.getCloth().getImageUrl(), model.itemImageUrl());
//            assertEquals(fitting.getImageUrl(), model.modelImageUrl());
//        }
//    }
//
//}
