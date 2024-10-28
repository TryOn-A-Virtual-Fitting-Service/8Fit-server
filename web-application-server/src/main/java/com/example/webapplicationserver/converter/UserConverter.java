package com.example.webapplicationserver.converter;

import com.example.webapplicationserver.dto.response.widget.ResponseWidgetDto;
import com.example.webapplicationserver.entity.User;

public class UserConverter {
    public static User toEntity(String deviceId) {
        return User.createUser()
                .deviceId(deviceId)
                .build();
    }

    public static ResponseWidgetDto toResponseWidgetDto(User user) {
        return new ResponseWidgetDto(user.getDeviceId());
    }
}
