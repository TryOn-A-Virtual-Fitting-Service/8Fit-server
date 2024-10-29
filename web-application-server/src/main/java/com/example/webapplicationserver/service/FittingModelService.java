package com.example.webapplicationserver.service;
import com.example.webapplicationserver.apiPayload.code.status.ErrorStatus;
import com.example.webapplicationserver.apiPayload.exception.handler.S3ExceptionHandler;
import com.example.webapplicationserver.apiPayload.exception.handler.UserExceptionHandler;
import com.example.webapplicationserver.converter.FittingModelConverter;
import com.example.webapplicationserver.dto.response.widget.ResponseFittingModelDto;
import com.example.webapplicationserver.entity.FittingModel;
import com.example.webapplicationserver.entity.User;
import com.example.webapplicationserver.repository.FittingModelRepository;
import com.example.webapplicationserver.repository.UserRepository;
import com.example.webapplicationserver.utils.S3Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class FittingModelService {
    private final FittingModelRepository fittingModelRepository;
    private final UserRepository userRepository;

    private final S3Utils s3Utils;

    @Transactional
    public ResponseFittingModelDto uploadFittingModel(String deviceId, MultipartFile image) {
        if (image.isEmpty()) {
            throw new S3ExceptionHandler(ErrorStatus.FILE_EMPTY);
        }

        // get user
        User user = userRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorStatus.USER_NOT_FOUND));

        // save fitting model
        String modelUrl = s3Utils.uploadImage(image);

        // save fitting model
        FittingModel fittingModel = FittingModelConverter.toEntity(modelUrl, user);
        fittingModelRepository.save(fittingModel);

        return FittingModelConverter.toResponseFittingModelDto(fittingModel);
    }
}
