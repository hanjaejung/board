package com.example.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SAME_USER_NAME(HttpStatus.CONFLICT, "userName is exist"),
    ;

    private HttpStatus status;
    private String message;
}
