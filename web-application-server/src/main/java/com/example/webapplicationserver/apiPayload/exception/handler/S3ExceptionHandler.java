package com.example.webapplicationserver.apiPayload.exception.handler;

import com.example.webapplicationserver.apiPayload.code.BaseErrorCode;
import com.example.webapplicationserver.apiPayload.exception.GeneralException;

public class S3ExceptionHandler extends GeneralException {
    public S3ExceptionHandler(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
