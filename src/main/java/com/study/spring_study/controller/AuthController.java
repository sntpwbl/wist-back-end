package com.study.spring_study.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.spring_study.dto.AccountCredentialsDTO;
import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.dto.TokenDTO;
import com.study.spring_study.dto.UserDTO;
import com.study.spring_study.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Used to manage authentication operations, such as login and sign up")
public class AuthController {

    @Autowired
    private AuthService service;
    
    @PostMapping(value = "/signin", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Authenticates a user", description = "Authenticates a user.", tags={"Authentication"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TokenDTO.class))),
            @Content(mediaType = "application/xml", array = @ArraySchema(schema = @Schema(implementation = TokenDTO.class)))
        }),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Invalid values", responseCode = "403", content = @Content),
        @ApiResponse(description= "User not found", responseCode = "404", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<TokenDTO> signIn(@RequestBody AccountCredentialsDTO dto) {
        return ResponseEntity.ok(service.signIn(dto));
    }
    
    @PostMapping("/user/signup")
    @Operation(summary = "Creates a new user", description = "Creates a new user with common role.", tags={"Authentication"}, responses = {
        @ApiResponse(description= "Created", responseCode = "201", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))),
            @Content(mediaType = "application/xml", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))
        }),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Invalid values", responseCode = "403", content = @Content),
        @ApiResponse(description= "Username already taken", responseCode = "403", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<UserDTO> signUp(@RequestBody AccountCredentialsDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.commonUserSignUp(dto));
    }
    
}
