package com.study.spring_study.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAllException(Exception ex, WebRequest request){
        ExceptionResponse er = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "500");
        return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse er = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "404");
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NullRequiredObjectException.class)
    public ResponseEntity<ExceptionResponse> handleNullRequiredObjectException(Exception ex, WebRequest request){
        ExceptionResponse er = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "400");
        return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidJWTAuthentication(Exception ex, WebRequest request){
        ExceptionResponse er = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), "403");
        return new ResponseEntity<>(er, HttpStatus.FORBIDDEN);
    }
}
