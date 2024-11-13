package com.example.webapplicationserver.apiPayload.code.status;

import com.example.webapplicationserver.apiPayload.code.BaseErrorCode;
import com.example.webapplicationserver.apiPayload.code.ErrorReasonDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatus implements BaseErrorCode {
    // General Errors
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 내부 에러"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "권한이 없습니다."),

    // USER(DEVICE)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "DEVICE404", "사용자를 찾을 수 없습니다. deviceId를 확인하세요"),

    // S3
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE500", "파일 업로드 중 에러가 발생했습니다."),
    FILE_EMPTY(HttpStatus.BAD_REQUEST, "FILE400", "파일이 비어있습니다."),

    // Fitting
    FITTING_POST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL500", "가상 피팅 POST 응답에서 에러가 발생했습니다."),
    FITTING_GET_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL500", "가상 피팅 GET 응답에서 에러가 발생했습니다."),
    FITTING_WEBCLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL500", "가상 피팅 서버 통신 과정에서 에러가 발생했습니다."),
    BYTES_CONVERSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "BYTES500", "바이트 변환 중 에러가 발생했습니다."),

    CLASSIFICATION_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL_PREDICT500", "이미지 분류 서버 통신 과정에서 에러가 발생했습니다."),
    CLASSIFICATION_INDEX_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL_PREDICT404", "이미지 분류 서버 응답 인덱싱 에러가 발생했습니다."),
    REMOVE_REMOVAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL_REMBG500", "배경 제거 서버 통신 과정에서 에러가 발생했습니다."),
    REMOVE_REMOVAL_ACTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL_REMBG404", "배경 제거 서버 응답이 비어있습니다."),
    UNSUPPORTED_CATEGORY(HttpStatus.BAD_REQUEST, "UNSUPPORTED_CATEGORY400", "지원하지 않는 카테고리입니다."),
    EMPTY_IMAGE_URL(HttpStatus.BAD_REQUEST, "EMPTY_IMAGE_URL400", "이미지 URL이 비어있습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    private final ErrorReasonDto errorReasonDto; // Prevent object creation every time by caching

    ErrorStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.errorReasonDto = new ErrorReasonDto(this.message, this.code, false, this.httpStatus); // create object
    }

    @Override
    public ErrorReasonDto getErrorReasonDto() {
        return this.errorReasonDto; // return cached object
    }



}
