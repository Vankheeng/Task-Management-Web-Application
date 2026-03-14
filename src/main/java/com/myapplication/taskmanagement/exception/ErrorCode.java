package com.myapplication.taskmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    PERMISSION_NOT_EXISTED(1008, "Permission not existed", HttpStatus.NOT_FOUND),
    DOB_INVALID(1009, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PHONENUMBER_EXISTED(1010, "Phone number existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1011, "Email existed", HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatusCode staticCode){
        this.code = code;
        this.message = message;
        this.staticCode = staticCode;
    }

    private int code = 1000;
    private String message;
    private HttpStatusCode staticCode;

    public HttpStatusCode getStaticCode() {
        return staticCode;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
