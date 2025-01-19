package com.study.spring_study.dto;

import java.util.List;

public record ProductDTO(
    Long id,
    String name,
    String description,
    String picture,
    boolean bought,
    List<StoreLinkDTO> storeLinks
){
}
