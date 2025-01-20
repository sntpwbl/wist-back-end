package com.study.spring_study.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public record StoreLinkDTO(
    String store,
    String url
) {}
