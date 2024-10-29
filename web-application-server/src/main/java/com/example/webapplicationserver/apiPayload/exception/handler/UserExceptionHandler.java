package com.example.webapplicationserver.apiPayload.exception.handler;

import com.example.webapplicationserver.apiPayload.code.BaseErrorCode;
import com.example.webapplicationserver.apiPayload.exception.GeneralException;

public class UserExceptionHandler extends GeneralException {
    public UserExceptionHandler(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
