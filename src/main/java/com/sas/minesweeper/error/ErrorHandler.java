package com.sas.minesweeper.error;

import com.sas.minesweeper.controller.response.ErrorResponse;
import com.sas.minesweeper.exception.DuplicateUsernameException;
import com.sas.minesweeper.exception.InvalidUserException;
import com.sas.minesweeper.exception.NotFoundGameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ErrorResponse> duplicatedUsernameExceptionHandling(DuplicateUsernameException exception, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ErrorResponse> invalidUserExceptionExceptionHandling(InvalidUserException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundGameException.class)
    public ResponseEntity<ErrorResponse> notFoundGameExceptionHandling(NotFoundGameException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
