package com.example.webapplicationserver.converter;

import com.example.webapplicationserver.entity.Cloth;
import com.example.webapplicationserver.entity.Fitting;
import com.example.webapplicationserver.entity.User;

public class FittingConverter {
    public static Fitting toEntity(String resultImageUrl, User user, Cloth cloth) {
        return Fitting.createFitting()
                .user(user)
                .cloth(cloth)
                .imageUrl(resultImageUrl)
                .llmAdvice(null)
                .build();
    }

}
