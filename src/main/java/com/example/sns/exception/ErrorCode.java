package com.example.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SAME_USER_NAME(HttpStatus.CONFLICT, "userName is exist"),
    USER_EXIST_NOT(HttpStatus.NOT_FOUND, "user do not exist"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "password wrong"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    ;

    private HttpStatus status;
    private String message;
}
