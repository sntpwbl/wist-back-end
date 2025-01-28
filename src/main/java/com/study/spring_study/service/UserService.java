package com.study.spring_study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.study.spring_study.model.User;
import com.study.spring_study.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository repository;
    
    public UserService(UserRepository repository){
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException(username + " not found.");
        }else return user;
    }
    
}
