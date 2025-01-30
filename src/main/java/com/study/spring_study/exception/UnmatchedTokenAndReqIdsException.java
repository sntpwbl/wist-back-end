package com.study.spring_study.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnmatchedTokenAndReqIdsException extends RuntimeException{
    public UnmatchedTokenAndReqIdsException() { super("Token user ID is not the same from the product's object."); }
}
