package com.study.spring_study.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.spring_study.dto.AccountCredentialsDTO;
import com.study.spring_study.dto.TokenDTO;
import com.study.spring_study.dto.UserDTO;
import com.study.spring_study.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/signin")
    public ResponseEntity<TokenDTO> signIn(@RequestBody AccountCredentialsDTO dto) {
        return ResponseEntity.ok(service.signIn(dto));
    }

    @PostMapping("/user/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody AccountCredentialsDTO dto){
        return ResponseEntity.ok(service.commonUserSignUp(dto));
    }
    
}
