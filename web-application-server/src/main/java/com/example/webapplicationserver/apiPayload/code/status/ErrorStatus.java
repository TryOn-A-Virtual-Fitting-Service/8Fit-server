package com.example.webapplicationserver.apiPayload.code.status;

import com.example.webapplicationserver.apiPayload.code.BaseErrorCode;
import com.example.webapplicationserver.apiPayload.code.ErrorReasonDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatus implements BaseErrorCode {
    // General Errors
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 내부 에러"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON4000", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON4001", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON4003", "권한이 없습니다."),
//
//    // 인증/인가 관련 에러
//    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "AUTH4000", "이미 존재하는 회원입니다."),
//    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH4004", "존재하지 않는 이메일입니다"),
//    MEMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH4003", "비밀번호가 일치하지 않습니다"),
//
//
//    // 게시글 관련 에러
//    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4004", "해당 게시글을 찾을 수 없습니다."),
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
