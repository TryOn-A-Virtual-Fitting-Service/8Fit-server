package com.example.webapplicationserver.dto.response.widget;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response fitting result DTO")
public record ResponseFittingResultDto (
        @Schema(description = "Result image URL")
        String resultImageUrl
) {
}
