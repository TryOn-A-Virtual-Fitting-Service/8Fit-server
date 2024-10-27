package com.example.webapplicationserver.apiPayload;

import com.example.webapplicationserver.apiPayload.code.BaseCode;
import com.example.webapplicationserver.apiPayload.code.ReasonDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"success", "code", "message", "result"})
public class ApiResponse<T> {
    @JsonProperty("success")
    private final boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    public static <T> ApiResponse<T> onSuccess(BaseCode baseCode, T result) {
        ReasonDto reasonDto = baseCode.getReasonDto();
        return new ApiResponse<>(true, reasonDto.code(), reasonDto.message(), result);
    }

    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }

}
