package com.example.sns.exception;

import com.example.sns.controller.response.PolymorphismResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GrobalControllerAdvice {

    //컨트롤러에서 익셉션을 처리하기 위해 만든 익셉션핸들러
    @ExceptionHandler(SnsException.class)
    public ResponseEntity<?> applicationHandler(SnsException e){
        log.error("error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(PolymorphismResponse.error(e.getErrorCode().name()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> databaseErrorHandler(RuntimeException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(PolymorphismResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
    }
}
