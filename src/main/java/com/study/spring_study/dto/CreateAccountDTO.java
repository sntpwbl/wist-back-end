package com.study.spring_study.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public record CreateAccountDTO(
    String userName,
    String fullName,
    String password,
    String repeatPassword
) {
    
}
