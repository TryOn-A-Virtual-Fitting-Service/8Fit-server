package com.example.webapplicationserver.service;

import com.example.webapplicationserver.apiPayload.code.status.ErrorStatus;
import com.example.webapplicationserver.apiPayload.exception.handler.UserExceptionHandler;
import com.example.webapplicationserver.converter.FittingModelConverter;
import com.example.webapplicationserver.converter.UserConverter;
import com.example.webapplicationserver.dto.request.widget.RequestWidgetInfoDto;
import com.example.webapplicationserver.dto.response.widget.ResponseWidgetDto;
import com.example.webapplicationserver.entity.FittingModel;
import com.example.webapplicationserver.entity.User;
import com.example.webapplicationserver.enums.DefaultModelUrl;
import com.example.webapplicationserver.repository.FittingModelRepository;
import com.example.webapplicationserver.repository.UserRepository;
import io.swagger.v3.core.converter.ModelConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WidgetService {

    private final UserRepository userRepository;
    private final FittingModelRepository fittingModelRepository;
    private static final String MALE = "male";
    private static final String FEMALE = "female";

    @Transactional
    public ResponseWidgetDto getWidgetInfo(RequestWidgetInfoDto requestWidgetInfoDto) {
        // Validate gender input
        validateGender(requestWidgetInfoDto.gender());

        // Find or create user
        User user = userRepository
                .findByDeviceId(requestWidgetInfoDto.deviceId())
                .orElseGet(() -> createUserWithDefaultModels(requestWidgetInfoDto));

        // Convert user to response DTO
        return UserConverter.toResponseWidgetDto(user);
    }

    private void validateGender(String gender) {
        if (!MALE.equalsIgnoreCase(gender) && !FEMALE.equalsIgnoreCase(gender)) {
            throw new UserExceptionHandler(ErrorStatus.GENDER_SETTING_NOT_FOUND);
        }
    }

    private User createUserWithDefaultModels(RequestWidgetInfoDto requestWidgetInfoDto) {
        // Create new user
        User newUser = userRepository.save(UserConverter.toEntity(requestWidgetInfoDto.deviceId()));

        // Initialize fitting models based on gender
        List<FittingModel> fittingModels = createDefaultFittingModels(requestWidgetInfoDto.gender(), newUser);

        // Save fitting models
        fittingModelRepository.saveAll(fittingModels);

        // Associate fitting models with the user
        newUser.addFittingModels(fittingModels.reversed());

        return newUser;
    }

    private List<FittingModel> createDefaultFittingModels(String gender, User user) {
        FittingModel primaryModel;
        FittingModel secondaryModel;

        if (MALE.equalsIgnoreCase(gender)) {
            primaryModel = FittingModelConverter.toEntity(DefaultModelUrl.MALE_MODEL.getUrl(), user);
            secondaryModel = FittingModelConverter.toEntity(DefaultModelUrl.FEMALE_MODEL.getUrl(), user);
        } else {
            primaryModel = FittingModelConverter.toEntity(DefaultModelUrl.FEMALE_MODEL.getUrl(), user);
            secondaryModel = FittingModelConverter.toEntity(DefaultModelUrl.MALE_MODEL.getUrl(), user);
        }

        return List.of(secondaryModel, primaryModel); // make primary model to display first (save in second time)
    }
}
