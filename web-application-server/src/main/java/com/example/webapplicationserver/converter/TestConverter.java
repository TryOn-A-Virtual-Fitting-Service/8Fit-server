package com.example.webapplicationserver.converter;

import com.example.webapplicationserver.dto.request.RequestTestDto;
import com.example.webapplicationserver.dto.response.ResponseTestDto;
import com.example.webapplicationserver.entity.User;

public class TestConverter {
    public static User toEntity(RequestTestDto requestTestDto) {
        return User.createUser()
                .deviceId(requestTestDto.deviceId())
                .build();
    }

    public static ResponseTestDto toResponseTestDto(User user) {
        return new ResponseTestDto(user.getDeviceId());
    }
}
