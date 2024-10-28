package com.example.webapplicationserver.apiPayload.code.status;

import com.example.webapplicationserver.apiPayload.code.BaseCode;
import com.example.webapplicationserver.apiPayload.code.ReasonDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessStatus implements BaseCode {
    // General Success
    OK(HttpStatus.OK, "COMMON200", "성공적으로 처리되었습니다."),
    CREATED(HttpStatus.CREATED, "COMMON201", "성공적으로 생성되었습니다."),
    ACCEPTED(HttpStatus.ACCEPTED, "COMMON202", "요청이 수락되었습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT, "COMMON204", "성공적으로 처리되었습니다."),

    // On Widget
    WIDGET_INFO_OK(HttpStatus.OK, "WIDGET200", "위젯 정보를 성공적으로 불러왔습니다."),
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    private final ReasonDto reasonDto; // Prevent object creation every time by caching

    SuccessStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.reasonDto = new ReasonDto(this.message, this.code, true, this.httpStatus); // create object
    }

    @Override
    public ReasonDto getReasonDto() {
        return this.reasonDto; // return cached object
    }

}
