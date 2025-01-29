package com.study.spring_study.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SignedUpUsernameException extends RuntimeException {
    public SignedUpUsernameException(){
        super("This username was already taken.");
    }
    public SignedUpUsernameException(String ex){
        super(ex);
    }
}
