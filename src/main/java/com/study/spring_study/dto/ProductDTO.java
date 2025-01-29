package com.study.spring_study.dto;

import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public record ProductDTO(
    Long id,
    Long userId,
    String name,
    String description,
    String picture,
    boolean bought,
    List<StoreLinkDTO> storeLinks
) {
}
