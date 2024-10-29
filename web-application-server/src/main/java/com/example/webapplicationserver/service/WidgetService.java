package com.example.webapplicationserver.service;

import com.example.webapplicationserver.converter.UserConverter;
import com.example.webapplicationserver.dto.response.widget.ResponseWidgetDto;
import com.example.webapplicationserver.entity.User;
import com.example.webapplicationserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WidgetService {
    private final UserRepository userRepository;

    @Transactional
    public ResponseWidgetDto getWidgetInfo(String deviceId) {
        User user = userRepository.findByDeviceId(deviceId)
                .orElseGet(() -> userRepository.save(UserConverter.toEntity(deviceId)));

        return UserConverter.toResponseWidgetDto(user);
    }
}
