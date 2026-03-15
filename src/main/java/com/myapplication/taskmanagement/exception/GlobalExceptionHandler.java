package com.myapplication.taskmanagement.exception;

import com.myapplication.taskmanagement.dto.response.APIResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

        @ExceptionHandler(value = RuntimeException.class)
        ResponseEntity<APIResponse> handlingRuntimeException(RuntimeException exception){
            APIResponse apiResponse = new APIResponse();

            apiResponse.setCode(1001);
            apiResponse.setMessage(exception.getMessage());
            return ResponseEntity.badRequest().body(apiResponse);
        }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse> handlingAppException(AppException exception) {
        exception.printStackTrace();
        ErrorCode errorCode = exception.getErrorCode();
        APIResponse apiResponse = new APIResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStaticCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<APIResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStaticCode())
                .body(APIResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse> handlingException(Exception exception) {
        exception.printStackTrace();
        APIResponse apiResponse = new APIResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStaticCode())
                .body(apiResponse);
    }

}
