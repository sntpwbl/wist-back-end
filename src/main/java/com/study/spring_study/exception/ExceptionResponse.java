package com.study.spring_study.exception;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ExceptionResponse implements Serializable{
    private Date timestamp;
    private String message;
    private String details;
    private String code;
}
