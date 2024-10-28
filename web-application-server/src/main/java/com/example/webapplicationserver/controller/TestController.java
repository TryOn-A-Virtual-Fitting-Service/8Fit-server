package com.example.webapplicationserver.controller;

import com.example.webapplicationserver.apiPayload.ApiResponse;
import com.example.webapplicationserver.apiPayload.code.status.SuccessStatus;
import com.example.webapplicationserver.dto.request.RequestTestDto;
import com.example.webapplicationserver.dto.response.ResponseTestDto;
import com.example.webapplicationserver.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Application is running");
    }


    @PostMapping
    public ApiResponse<ResponseTestDto> checkDeviceId(
            @RequestBody RequestTestDto requestTestDto
    ) {
        ResponseTestDto responseTestDto = testService.saveTest(requestTestDto);
        return ApiResponse.onSuccess(SuccessStatus.OK, responseTestDto);
    }



}
