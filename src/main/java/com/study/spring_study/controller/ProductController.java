package com.study.spring_study.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.model.Product;
import com.study.spring_study.model.StoreLink;
import com.study.spring_study.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;


@RestController
@RequestMapping("/product")
@Tag(name = "Product", description = "Used to manage products operations")
public class ProductController {
    
    @Autowired
    private ProductService service;

    @GetMapping("/user")
    @Operation(summary = "Returns a users' products", description = "Returns all created and storaged products. Does not return XML.", tags={"Product"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<List<EntityModel<ProductDTO>>> getUserProducts(HttpServletRequest request) {
        return ResponseEntity.ok(service.findProductsByUserId(request));
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Returns a product", description = "Returns a product by its identification.", tags={"Product"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)),
            @Content(mediaType = "application/xml", schema = @Schema(implementation = ProductDTO.class)),
        }),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Not found", responseCode = "404", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ProductDTO>> findById(@PathVariable Long id, HttpServletRequest request) {
        EntityModel<ProductDTO> model = service.findById(id, request);
        return ResponseEntity.ok().body(model);
    }


    @PostMapping(value = "/create", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Creates a product", description = "Creates a product by passing all of the required fields.", tags={"Product"}, responses = {
        @ApiResponse(description= "Success", responseCode = "201", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)),
            @Content(mediaType = "application/xml", schema = @Schema(implementation = ProductDTO.class)),
        }),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ProductDTO>> createProduct(@RequestBody ProductDTO dto, HttpServletRequest request) {
        List<StoreLink> links = dto.storeLinks().stream().map(linkRequest -> {
            StoreLink link = new StoreLink();
            link.setStore(linkRequest.store());
            link.setUrl(linkRequest.url());
            return link;
        }).collect(Collectors.toList());

        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPicture(dto.picture());

        EntityModel<ProductDTO> savedProduct = service.createProduct(product, links, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }
    

    @PutMapping(value = "/update/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Updates a product", description = "Updates a product using its identification and all of the required fields.", tags={"Product"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)),
            @Content(mediaType = "application/xml", schema = @Schema(implementation = ProductDTO.class)),
        }),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Not found", responseCode = "404", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ProductDTO>> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long id, HttpServletRequest request) {
        EntityModel<ProductDTO> updatedProduct = service.updateProduct(productDTO, id, request);
        return ResponseEntity.ok().body(updatedProduct);
    }


    @PatchMapping(value = "/{id}/{status}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Changes the products bought status", description = "Use the identification of a product and a boolean value to change its bought status.", tags={"Product"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)),
            @Content(mediaType = "application/xml", schema = @Schema(implementation = ProductDTO.class)),
        }),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Not found", responseCode = "404", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ProductDTO>> changeProductBoughtStatus(@PathVariable Long id, @PathVariable boolean status, HttpServletRequest request){
        EntityModel<ProductDTO> updatedProductDTO = service.changeProductBoughtStatus(id, status, request);
        return ResponseEntity.ok().body(updatedProductDTO);
    }


    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deletes a product", description = "Deletes a product by using its identification.", tags={"Product"}, responses = {
        @ApiResponse(description= "Success", responseCode = "204", content = @Content),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Not found", responseCode = "404", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<String> deleteProduct(@PathVariable Long id, HttpServletRequest request){
        service.deleteProduct(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
