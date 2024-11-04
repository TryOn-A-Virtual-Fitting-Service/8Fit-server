package com.example.webapplicationserver.dto.response.widget;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO containing the model URL for model upload request.")
public record ResponseFittingModelDto(
        @Schema(description = "URL of the user custom model image uploaded.")
        String modelUrl
) {
}
