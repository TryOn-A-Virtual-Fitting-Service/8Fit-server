package com.example.webapplicationserver.converter;

import com.example.webapplicationserver.dto.response.widget.ResponseFittingModelDto;
import com.example.webapplicationserver.entity.FittingModel;
import com.example.webapplicationserver.entity.User;

public class FittingModelConverter {
    public static FittingModel toEntity(String modelUrl, User user) {
        return FittingModel.createFittingModel()
                .user(user)
                .imageUrl(modelUrl)
                .build();
    }

    public static ResponseFittingModelDto toResponseFittingModelDto(FittingModel fittingModel) {
        return new ResponseFittingModelDto(fittingModel.getImageUrl());
    }
}
