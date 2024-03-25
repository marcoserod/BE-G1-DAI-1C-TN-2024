package com.dai.dai.exception.handler;

import com.dai.dai.exception.DaiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class DaiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<DaiException> handleConflict(Exception exception){
        log.error("[DaiExceptionHandler] Generating exception for error: {}", exception.getMessage());

        var ex = DaiException.builder()
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
