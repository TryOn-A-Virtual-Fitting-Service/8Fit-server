package com.example.webapplicationserver.converter;

import com.example.webapplicationserver.dto.response.widget.ResponseWidgetDto;
import com.example.webapplicationserver.entity.FittingModel;
import com.example.webapplicationserver.entity.User;

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
        return user.getFittingModels().stream()
                .map(fittingModel -> new ResponseWidgetDto.Model(
                        fittingModel.getId(),
                        createFittings(fittingModel),
                        fittingModel.getImageUrl()
                ))
                .toList();
    }

    private static List<ResponseWidgetDto.Model.Fitting> createFittings(FittingModel fittingModel) {
        return fittingModel.getFittings().stream()
                .map(fitting -> new ResponseWidgetDto.Model.Fitting(
                        fitting.getId(),
                        fitting.getImageUrl(),
                        fitting.getCloth().getImageUrl()
                ))
                .toList();
    }
}
