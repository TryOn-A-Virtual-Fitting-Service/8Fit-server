package com.example.webapplicationserver.apiPayload.code;

import org.springframework.http.HttpStatus;

public record ErrorReasonDto(String message, String code, boolean isSuccess, HttpStatus httpStatus) {
}
