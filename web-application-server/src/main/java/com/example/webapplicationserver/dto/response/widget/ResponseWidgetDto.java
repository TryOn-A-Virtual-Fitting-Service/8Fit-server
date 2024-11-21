package com.example.webapplicationserver.dto.response.widget;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response DTO containing the widget information for the device.")
public record ResponseWidgetDto(
        @Schema(description = "List of models for the widget.")
        List<Model> models
) {
    public record Model(
            @Schema(description = "Model ID.")
            Long modelId,
            @Schema(description = "URL for the item image.")
            List<Fitting> fittings,
            @Schema(description = "URL for the model image.")
            String modelImageUrl
    ) {
        public record Fitting(
                @Schema(description = "Fitting ID.")
                Long fittingId,
                @Schema(description = "URL for the fitting image.")
                String fittingImageUrl,
                @Schema(description = "item image URL.")
                String itemImageUrl
        ) {
        }
    }
}
