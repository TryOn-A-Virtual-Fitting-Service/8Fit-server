package com.example.webapplicationserver.controller;

import com.example.webapplicationserver.apiPayload.ApiResponse;
import com.example.webapplicationserver.apiPayload.code.status.SuccessStatus;
import com.example.webapplicationserver.dto.response.widget.ResponseFittingModelDto;
import com.example.webapplicationserver.dto.response.widget.ResponseWidgetDto;
import com.example.webapplicationserver.service.FittingModelService;
import com.example.webapplicationserver.service.WidgetService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController("/widget")
@RequiredArgsConstructor
public class WidgetController {
    private final WidgetService widgetService;
    private final FittingModelService fittingModelService;

    @GetMapping({"{deviceId}"})
    public ApiResponse<ResponseWidgetDto> getWidget(
            @PathVariable("deviceId") @NotEmpty String deviceId
    ) {
        ResponseWidgetDto responseWidgetDto = widgetService.getWidgetInfo(deviceId);
        return ApiResponse.onSuccess(SuccessStatus.WIDGET_INFO_OK, responseWidgetDto);
    }

    @PostMapping({"{deviceId}/model"})
    public ApiResponse<ResponseFittingModelDto> getWidgetModel(
            @PathVariable("deviceId") @NotEmpty String deviceId,
            @RequestParam("image") MultipartFile image) {
        ResponseFittingModelDto responseFittingModelDto = fittingModelService.uploadFittingModel(deviceId, image);
        return ApiResponse.onSuccess(SuccessStatus.FILE_UPLOAD_OK, responseFittingModelDto);
    }


}
