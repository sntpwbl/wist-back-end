package com.study.spring_study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.spring_study.mapper.ModelMapper;
import com.study.spring_study.repository.ListRepository;
import com.study.spring_study.repository.UserRepository;
import com.study.spring_study.security.JwtUtil;

@Service
public class ListService {
    
    @Autowired
    private ListRepository listRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JwtUtil jwtUtil;

    

}
