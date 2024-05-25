package com.dai.dai.exception.handler;

import com.dai.dai.exception.DaiException;
import com.dai.dai.exception.SortCriteriaNotAllowedException;
import com.dai.dai.exception.TmdbNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class DaiExceptionHandler extends ResponseEntityExceptionHandler {

    private String BAD_REQUEST_MESSAGE = "BAD REQUEST: Revise el swagger para validar la manera correcta de consumir el endpoint.";
    private String SORT_CRITERIA_NOT_ALLOWED = "BAD REQUEST: Revise el swagger para validar los metodos de ordenamiento tolerados.";

    @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<DaiException> handleGenericException(MethodArgumentTypeMismatchException exception){
        log.error("[DaiExceptionHandler] Generating exception for error: {}", exception.getMessage());

        var ex = DaiException.builder()
                .message(BAD_REQUEST_MESSAGE)
                .build();

        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { SortCriteriaNotAllowedException.class})
    protected ResponseEntity<DaiException> handleSortCriteriaNotAllowedException(SortCriteriaNotAllowedException exception){
        log.error("[DaiExceptionHandler] Generating exception for error: {}", exception.getMessage());

        var ex = DaiException.builder()
                .message(SORT_CRITERIA_NOT_ALLOWED)
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

    @ExceptionHandler(value = { ConflictException.class })
    protected ResponseEntity<DaiException> handleConflictGenericException(Exception exception){
        log.error("[DaiExceptionHandler] Generating exception for error: {}", exception.getMessage());

        var ex = DaiException.builder()
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<DaiException> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error("[DaiExceptionHandler] Generating exception for error: {}", exception.getMessage());
        var ex = DaiException.builder()
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }
}
