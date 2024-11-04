package com.example.webapplicationserver.apiPayload;

import com.example.webapplicationserver.apiPayload.code.BaseCode;
import com.example.webapplicationserver.apiPayload.code.ReasonDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"success", "code", "message", "result"})
public class ApiResponseWrapper<T> {
    @JsonProperty("success")
    @Schema(description = "API 호출 성공 여부", example = "true/false")
    private final boolean isSuccess;

    @Schema(description = "응답 코드", example = "COMMON200/FILE400")
    private final String code;

    @Schema(description = "응답 메시지", example = "성공적으로 처리되었습니다./잘못된 요청입니다.")
    private final String message;

    @Schema(description = "응답 데이터")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    public static ApiResponseWrapper<?> onSuccess(BaseCode baseCode) {
        ReasonDto reasonDto = baseCode.getReasonDto();
        return new ApiResponseWrapper<>(true, reasonDto.code(), reasonDto.message(), null);
    }

    public static <T> ApiResponseWrapper<T> onSuccess(BaseCode baseCode, T result) {
        ReasonDto reasonDto = baseCode.getReasonDto();
        return new ApiResponseWrapper<>(true, reasonDto.code(), reasonDto.message(), result);
    }

    public static <T> ApiResponseWrapper<T> onFailure(String code, String message, T data) {
        return new ApiResponseWrapper<>(false, code, message, data);
    }

}
