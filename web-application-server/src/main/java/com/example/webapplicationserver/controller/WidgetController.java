package com.example.webapplicationserver.controller;

import com.example.webapplicationserver.apiPayload.ApiResponse;
import com.example.webapplicationserver.apiPayload.code.status.SuccessStatus;
import com.example.webapplicationserver.dto.response.widget.ResponseWidgetDto;
import com.example.webapplicationserver.service.WidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/widget")
@RequiredArgsConstructor
public class WidgetController {
    private final WidgetService widgetService;

    @GetMapping({"{deviceId}"})
    public ApiResponse<ResponseWidgetDto> getWidget(@PathVariable("deviceId") String deviceId) {
        ResponseWidgetDto responseWidgetDto = widgetService.getWidgetInfo(deviceId);
        return ApiResponse.onSuccess(SuccessStatus.WIDGET_INFO_OK, responseWidgetDto);
    }

}
