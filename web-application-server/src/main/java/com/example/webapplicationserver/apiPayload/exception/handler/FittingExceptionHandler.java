package com.example.webapplicationserver.apiPayload.exception.handler;

import com.example.webapplicationserver.apiPayload.code.status.ErrorStatus;
import com.example.webapplicationserver.apiPayload.exception.GeneralException;

public class FittingExceptionHandler extends GeneralException {
    public FittingExceptionHandler(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
