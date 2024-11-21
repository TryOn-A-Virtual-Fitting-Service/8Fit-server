package com.example.webapplicationserver.converter;

import com.example.webapplicationserver.dto.response.widget.ResponseFittingResultDto;
import com.example.webapplicationserver.entity.Cloth;
import com.example.webapplicationserver.entity.Fitting;
import com.example.webapplicationserver.entity.FittingModel;
import com.example.webapplicationserver.entity.User;

public class FittingConverter {
    public static Fitting toEntity(String resultImageUrl, FittingModel fittingModel, Cloth cloth) {
        return Fitting.createFitting()
                .fittingModel(fittingModel)
                .cloth(cloth)
                .imageUrl(resultImageUrl)
                .llmAdvice(null)
                .build();
    }

    public static ResponseFittingResultDto toResponseFittingResultDto(Fitting fitting) {
        return new ResponseFittingResultDto(fitting.getImageUrl());
    }

}
