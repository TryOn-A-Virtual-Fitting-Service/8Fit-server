package com.example.webapplicationserver.service;

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
    private final String MALE = "male";
    private final String FEMALE = "female";

    @Transactional
    public ResponseWidgetDto getWidgetInfo(RequestWidgetInfoDto requestWidgetInfoDto) {
        Optional<User> user = userRepository.findByDeviceId(requestWidgetInfoDto.deviceId());

        if (user.isEmpty()) {
            // save user
            User newUser = userRepository.save(UserConverter.toEntity(requestWidgetInfoDto.deviceId()));

            FittingModel firstModel, secondModel;

            if (requestWidgetInfoDto.gender().equalsIgnoreCase(MALE)) {
                firstModel = FittingModelConverter.toEntity(DefaultModelUrl.MALE_MODEL.getUrl(), newUser);
                secondModel = FittingModelConverter.toEntity(DefaultModelUrl.FEMALE_MODEL.getUrl(), newUser);
            } else {
                firstModel = FittingModelConverter.toEntity(DefaultModelUrl.FEMALE_MODEL.getUrl(), newUser);
                secondModel = FittingModelConverter.toEntity(DefaultModelUrl.MALE_MODEL.getUrl(), newUser);
            }
            newUser.addFittingModels(List.of(firstModel, secondModel));

            fittingModelRepository.save(firstModel);
            fittingModelRepository.save(secondModel);

            return UserConverter.toResponseWidgetDto(newUser);
        }

        return UserConverter.toResponseWidgetDto(user.get());
    }
}
