package com.study.spring_study.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/adm")
@Tag(name = "Admin", description = "Used to manage administrator operations")
public class AdminController {
    @Autowired
    private ProductService service;

    @GetMapping("/product/all")
    @Operation(summary = "Returns all created products", description = "Returns all created and storaged products. Does not return XML.", tags={"Admin"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description= "Forbidden for common users", responseCode = "403", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<List<EntityModel<ProductDTO>>> getAllProducts() {
        return ResponseEntity.ok(service.findAll());
    }
}
