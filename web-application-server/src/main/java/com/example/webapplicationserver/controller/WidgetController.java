package com.example.webapplicationserver.controller;

import com.example.webapplicationserver.apiPayload.ApiResponseWrapper;
import com.example.webapplicationserver.apiPayload.code.status.SuccessStatus;
import com.example.webapplicationserver.dto.response.widget.ResponseFittingModelDto;
import com.example.webapplicationserver.dto.response.widget.ResponseWidgetDto;
import com.example.webapplicationserver.service.FittingModelService;
import com.example.webapplicationserver.service.FittingService;
import com.example.webapplicationserver.service.WidgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/widget")
@RequiredArgsConstructor
@Tag(name = "Widget API", description = "set of widget endpoints")
public class WidgetController {
    private final WidgetService widgetService;
    private final FittingModelService fittingModelService;
    private final FittingService fittingService;

    @Operation(summary = "Get widget information for specific device", description = "Get widget information by device id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Widget information is successfully retrieved"),
    })
    @GetMapping({"{deviceId}"})
    public ApiResponseWrapper<ResponseWidgetDto> getWidget(
            @Parameter(description = "Device ID", required = true)
            @PathVariable("deviceId") @NotEmpty String deviceId
    ) {
        ResponseWidgetDto responseWidgetDto = widgetService.getWidgetInfo(deviceId);
        return ApiResponseWrapper.onSuccess(SuccessStatus.WIDGET_INFO_OK, responseWidgetDto);
    }

    @Operation(summary = "Upload fitting model image", description = "Upload fitting model image for specific device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fitting model image is successfully uploaded"),
            @ApiResponse(responseCode = "404", description = "User Not Found(DEVICE404)"),
            @ApiResponse(responseCode = "400", description = "File is empty(FILE400)"),
            @ApiResponse(responseCode = "500", description = "File upload error(FILE500)"),
    })
    @PostMapping({"{deviceId}/model"})
    public ApiResponseWrapper<ResponseFittingModelDto> uploadFittingModel(
            @Parameter(description = "Device ID", required = true)
            @PathVariable("deviceId") @NotEmpty String deviceId,

            @RequestParam("image") MultipartFile image
    ) {
        ResponseFittingModelDto responseFittingModelDto = fittingModelService.uploadFittingModel(deviceId, image);
        return ApiResponseWrapper.onSuccess(SuccessStatus.FILE_UPLOAD_OK, responseFittingModelDto);
    }

    @PostMapping("{deviceId}/cloth")
    public ApiResponseWrapper<?> getFittingResult(
            @PathVariable("deviceId") String deviceId,
            @RequestParam("image") MultipartFile image
    ) {
        fittingService.tryOnCloth(deviceId, image);
        return ApiResponseWrapper.onSuccess(SuccessStatus.FILE_UPLOAD_OK);
    }


}
