package com.example.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //이런식으로 에러코드를 정하면 서버에서 에러가 발생시 403이런식으로 에러가 오지 않고 소스로 적어둔
    //에러로보여지게 되어 좀 더 깔끔하게 에러가 보여진다
    SAME_USER_NAME(HttpStatus.CONFLICT, "userName is exist"),
    USER_EXIST_NOT(HttpStatus.NOT_FOUND, "user do not exist"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "password wrong"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "Board not founded"),
    ALREADY_LIKED_BOARD(HttpStatus.CONFLICT, "user already like the board"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    ;

    private HttpStatus status;
    private String message;
}
