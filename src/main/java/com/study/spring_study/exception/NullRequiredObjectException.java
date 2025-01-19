package com.study.spring_study.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullRequiredObjectException extends RuntimeException{
    
    public NullRequiredObjectException(){
        super("Required object sent as null.");
    }
    public NullRequiredObjectException(String ex){
        super(ex);
    }
}
