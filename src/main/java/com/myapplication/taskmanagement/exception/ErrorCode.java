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
    DOB_INVALID(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PHONENUMBER_EXISTED(1009, "Phone number existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1010, "Email existed", HttpStatus.BAD_REQUEST),

    TEAM_NOT_EXISTED(2001, "Team not existed", HttpStatus.NOT_FOUND),

    TEAMMEMBER_NOT_EXISTED(3001, "This user is not member of this team", HttpStatus.NOT_FOUND),

    PROJECT_NOT_EXISTED(4001, "Project not existed", HttpStatus.NOT_FOUND),

    TASKLIST_NOT_EXISTED(5001, "TaskList not existed", HttpStatus.NOT_FOUND),

    TASK_NOT_EXISTED(6001, "Task not existed", HttpStatus.NOT_FOUND),
    INVALID_DEADLINE(6002, "Deadline cannot be in the past", HttpStatus.BAD_REQUEST),

    STATUS_NOT_EXISTED(7001, "Status not existed", HttpStatus.NOT_FOUND),

    COMMENT_NOT_EXISTED(8001, "Comment not existed", HttpStatus.NOT_FOUND),

    TASKASSIGNMENT_NOT_EXISTED(9001, "Task assignment not existed", HttpStatus.NOT_FOUND),
    USER_ALREADY_ASSIGNED(9002, "User already assigned to this task", HttpStatus.BAD_REQUEST),

    TASKATTACHMENT_NOT_EXISTED(1101, "Task attachment not existed", HttpStatus.NOT_FOUND),

    NOTIFICATION_NOT_EXISTED(12001, "Notification not existed", HttpStatus.NOT_FOUND),;


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
