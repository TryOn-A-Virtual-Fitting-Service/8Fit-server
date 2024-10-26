package com.example.webapplicationserver.apiPayload.exception.handler;

import com.example.webapplicationserver.apiPayload.code.BaseErrorCode;
import com.example.webapplicationserver.apiPayload.exception.GeneralException;

public class TestExceptionHandler extends GeneralException {
    public TestExceptionHandler(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }

}
