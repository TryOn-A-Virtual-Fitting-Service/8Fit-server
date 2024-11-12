package com.example.webapplicationserver.dto.external;

public record PredictResponseDto(
        Integer class_idx,
        String class_name,
        Double confidence
) {
}
