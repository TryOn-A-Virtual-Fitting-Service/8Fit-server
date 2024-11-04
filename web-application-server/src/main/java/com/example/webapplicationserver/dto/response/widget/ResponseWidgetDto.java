package com.example.webapplicationserver.dto.response.widget;

import io.swagger.v3.oas.annotations.media.Schema;

// temporary setting. need to include fitting model information and item, history. etc
@Schema(description = "Response DTO containing the widget information for the device.")
public record ResponseWidgetDto(
        String deviceId
) {

}
