package com.study.spring_study.dto;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public record TokenDTO(
    String userName,
    boolean authenticated,
    Date createdAt,
    Date expiration,
    String accessToken,
    String refreshToken
) {
    
}
