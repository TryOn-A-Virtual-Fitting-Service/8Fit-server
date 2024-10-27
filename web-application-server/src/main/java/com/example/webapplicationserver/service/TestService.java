package com.example.webapplicationserver.service;

import com.example.webapplicationserver.apiPayload.code.status.ErrorStatus;
import com.example.webapplicationserver.apiPayload.exception.handler.TestExceptionHandler;
import com.example.webapplicationserver.converter.TestConverter;
import com.example.webapplicationserver.dto.request.RequestTestDto;
import com.example.webapplicationserver.dto.response.ResponseTestDto;
import com.example.webapplicationserver.entity.User;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    public ResponseTestDto saveTest(RequestTestDto requestTestDto) {
        if (requestTestDto.deviceId().isBlank()) {
            // throw exception if deviceId is empty or contains only whitespace
            throw new TestExceptionHandler(ErrorStatus.BAD_REQUEST);
        }
        // request dto -> entity
        User user = TestConverter.toEntity(requestTestDto);
        // save to db, etc ..

        // entity -> response dto
        return TestConverter.toResponseTestDto(user);
    }

}
