package com.example.webapplicationserver.converter;

import com.example.webapplicationserver.dto.response.widget.ResponseFittingModelDto;
import com.example.webapplicationserver.dto.response.widget.ResponseWidgetDto;
import com.example.webapplicationserver.entity.Fitting;
import com.example.webapplicationserver.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {
    public static User toEntity(String deviceId) {
        return User.createUser()
                .deviceId(deviceId)
                .build();
    }

    public static ResponseWidgetDto toResponseWidgetDto(User user) {
        return new ResponseWidgetDto(createModels(user));
    }

    private static List<ResponseWidgetDto.Model> createModels(User user) {
        return user.getFittings().stream()
                .map(fitting -> new ResponseWidgetDto.Model(
                        fitting.getId(),
                        fitting.getCloth().getImageUrl(),
                        fitting.getImageUrl()
                ))
                .toList();
    }
}
