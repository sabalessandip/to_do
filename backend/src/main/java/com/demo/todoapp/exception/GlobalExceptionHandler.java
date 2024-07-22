package com.demo.todoapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody GenericErrorResponse handleException(RuntimeException exception) {
        return new GenericErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()
        );
    }
}
