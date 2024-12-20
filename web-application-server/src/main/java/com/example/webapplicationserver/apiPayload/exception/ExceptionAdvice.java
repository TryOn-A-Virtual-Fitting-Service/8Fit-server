package com.example.webapplicationserver.apiPayload.exception;

import com.example.webapplicationserver.apiPayload.ApiResponseWrapper;
import com.example.webapplicationserver.apiPayload.code.ErrorReasonDto;
import com.example.webapplicationserver.apiPayload.code.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> validation(
            ConstraintViolationException e,
            WebRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));

        return handleExceptionInternalConstraint(e, ErrorStatus.valueOf(errorMessage), HttpHeaders.EMPTY,
                request);
    }

    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().stream()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage())
                            .orElse("");
                    errors.merge(fieldName, errorMessage,
                            (existingErrorMessage, newErrorMessage) -> existingErrorMessage
                                    + ", " + newErrorMessage);
                });

        return handleExceptionInternalArgs(e, HttpHeaders.EMPTY, ErrorStatus.valueOf("_BAD_REQUEST"), request,
                errors);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(
            Exception e,
            WebRequest request) {
        e.printStackTrace();

        return handleExceptionInternalFalse(e, ErrorStatus.INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY,
                ErrorStatus.INTERNAL_SERVER_ERROR.getErrorReasonDto().httpStatus(), request, e.getMessage());
    }

    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<Object> onThrowException(
            GeneralException generalException,
            HttpServletRequest request) {
        ErrorReasonDto errorReasonHttpStatus = generalException.getErrorReasonDto();
        return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(
            Exception e,
            ErrorReasonDto errorReasonDto,
            HttpHeaders headers,
            HttpServletRequest request) {

        ApiResponseWrapper<Object> body = ApiResponseWrapper.onFailure(errorReasonDto.code(), errorReasonDto.message(), null);
        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorReasonDto.httpStatus(),
                webRequest);
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(
            Exception e,
            ErrorStatus errorStatus,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request,
            String errorPoint) {
        ApiResponseWrapper<Object> body = ApiResponseWrapper.onFailure(errorStatus.getErrorReasonDto().code(), errorStatus.getErrorReasonDto().message(), errorPoint);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                status,
                request);
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(
            Exception e,
            HttpHeaders headers,
            ErrorStatus errorStatus,
            WebRequest request,
            Map<String, String> errorArgs) {
        ApiResponseWrapper<Object> body = ApiResponseWrapper.onFailure(errorStatus.getErrorReasonDto().code(), errorStatus.getErrorReasonDto().code(), errorArgs);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorStatus.getErrorReasonDto().httpStatus(),
                request);
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(
            Exception e,
            ErrorStatus errorStatus,
            HttpHeaders headers,
            WebRequest request) {
        ApiResponseWrapper<Object> body = ApiResponseWrapper.onFailure(errorStatus.getErrorReasonDto().code(), errorStatus.getErrorReasonDto().message(), null);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorStatus.getErrorReasonDto().httpStatus(),
                request);
    }

}
