package com.example.webapplicationserver.service;

import com.example.webapplicationserver.dto.response.widget.ResponseWidgetDto;
import com.example.webapplicationserver.entity.User;
import com.example.webapplicationserver.model.UserFixture;
import com.example.webapplicationserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WidgetServiceMockTest {
    private WidgetService widgetService;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // repository initialization
        userRepository = Mockito.mock(UserRepository.class);

        // service initialization
        widgetService = new WidgetService(
                userRepository
        );
    }

    @Test
    void getWidgetInfoTest_UserExists() {
        // Arrange
        String deviceId = "existing-device-id";
        User existingUser = UserFixture.createTestUserWithDeviceId(deviceId);

        // Mocking userRepository to return an existing user
        when(userRepository.findByDeviceId(deviceId)).thenReturn(Optional.of(existingUser));

        // Act
        ResponseWidgetDto response = widgetService.getWidgetInfo(deviceId);

        // Assert
        assertEquals(deviceId, response.deviceId());
        verify(userRepository, times(1)).findByDeviceId(deviceId);
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void getWidgetInfoTest_UserDoesNotExist() {
        // Arrange
        String deviceId = "non-existing-device-id";
        User newUser = UserFixture.createTestUserWithDeviceId(deviceId);

        // Mocking userRepository to return an empty optional
        when(userRepository.findByDeviceId(deviceId)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        ResponseWidgetDto response = widgetService.getWidgetInfo(deviceId);

        // Assert
        assertEquals(deviceId, response.deviceId());
        verify(userRepository, times(1)).findByDeviceId(deviceId);
        verify(userRepository, times(1)).save(any(User.class));
    }

}
