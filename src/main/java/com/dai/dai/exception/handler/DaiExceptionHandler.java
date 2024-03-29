package com.dai.dai.exception.handler;

import com.dai.dai.exception.DaiException;
import com.dai.dai.exception.TmdbNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class DaiExceptionHandler extends ResponseEntityExceptionHandler {

    private String BAD_REQUEST_MESSAGE = "BAD REQUEST: Revise el swagger para validar la manera correcta de consumir el endpoint.";

    @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
    protected ResponseEntity<DaiException> handleGenericException(MethodArgumentTypeMismatchException exception){
        log.error("[DaiExceptionHandler] Generating exception for error: {}", exception.getMessage());

        var ex = DaiException.builder()
                .message(BAD_REQUEST_MESSAGE)
                .build();

        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = TmdbNotFoundException.class)
    protected ResponseEntity<DaiException> handlTmdbNotFoundExceptionException(TmdbNotFoundException exception){
        log.error("[DaiExceptionHandler] Generating exception for error: {}", exception.getMessage());

        var ex = DaiException.builder()
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<DaiException> handleGenericException(Exception exception){
        log.error("[DaiExceptionHandler] Generating exception for error: {}", exception.getMessage());

        var ex = DaiException.builder()
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
