package com.example.webapplicationserver.apiPayload.exception;

import com.example.webapplicationserver.apiPayload.code.BaseErrorCode;
import com.example.webapplicationserver.apiPayload.code.ErrorReasonDto;
import lombok.Getter;



@Getter
public class GeneralException extends RuntimeException {
    private final BaseErrorCode baseErrorCode;

    public GeneralException(BaseErrorCode baseErrorCode) {
        this.baseErrorCode = baseErrorCode;
    }

    public ErrorReasonDto getErrorReasonDto() {
        return this.baseErrorCode.getErrorReasonDto();
    }

}
