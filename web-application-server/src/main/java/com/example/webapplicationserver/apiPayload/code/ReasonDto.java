package com.example.webapplicationserver.apiPayload.code;


import org.springframework.http.HttpStatus;

public record ReasonDto(String message, String code, boolean isSuccess, HttpStatus httpStatus) {
}
