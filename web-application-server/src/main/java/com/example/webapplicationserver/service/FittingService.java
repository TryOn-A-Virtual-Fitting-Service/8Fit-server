package com.example.webapplicationserver.service;

import com.example.webapplicationserver.apiPayload.code.status.ErrorStatus;
import com.example.webapplicationserver.apiPayload.exception.handler.S3ExceptionHandler;
import com.example.webapplicationserver.apiPayload.exception.handler.UserExceptionHandler;
import com.example.webapplicationserver.converter.ClothConverter;
import com.example.webapplicationserver.converter.FittingConverter;
import com.example.webapplicationserver.entity.Cloth;
import com.example.webapplicationserver.entity.Fitting;
import com.example.webapplicationserver.entity.User;
import com.example.webapplicationserver.repository.ClothRepository;
import com.example.webapplicationserver.repository.FittingRepository;
import com.example.webapplicationserver.repository.UserRepository;
import com.example.webapplicationserver.utils.S3Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FittingService {
    // repository layer
    private final UserRepository userRepository;
    private final FittingRepository fittingRepository;
    private final ClothRepository clothRepository;

    // utilize S3
    private final S3Utils s3Utils;

    public void tryOnCloth(String deviceId, MultipartFile image) {
        if (image.isEmpty()) {
            throw new S3ExceptionHandler(ErrorStatus.FILE_EMPTY);
        }
        // get user
        User user = userRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorStatus.USER_NOT_FOUND));

        // upload fitting result image and Cloth image
        String clothImageUrl = s3Utils.uploadImage(image);
        String resultImageUrl = s3Utils.uploadImage(image); // FIXME: should be fitting result image

        // save Cloth entity
        Cloth cloth = ClothConverter.toEntity(clothImageUrl);
        clothRepository.save(cloth);

        // save Fitting entity
        Fitting fitting = FittingConverter.toEntity(resultImageUrl, user, cloth);
        fittingRepository.save(fitting);
    }
}
