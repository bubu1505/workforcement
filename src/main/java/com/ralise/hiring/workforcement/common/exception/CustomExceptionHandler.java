package com.ralise.hiring.workforcement.common.exception;

import com.ralise.hiring.workforcement.common.model.response.Response;
import com.ralise.hiring.workforcement.common.model.response.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ResponseStatus status = new ResponseStatus(StatusCode.NOT_FOUND.getCode(), ex.getMessage());
        return new ResponseEntity<>(new Response<>(null, null, status), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleAllExceptions(Exception ex) {
        ResponseStatus status = new ResponseStatus(StatusCode.INTERNAL_SERVER_ERROR.getCode(), ex.getMessage());
        return new ResponseEntity<>(new Response<>(null, null, status), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
