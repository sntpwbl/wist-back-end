package com.study.spring_study.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    public JwtUtil(JwtTokenProvider provider){
        jwtTokenProvider = provider;
    }

    public Long getUserIdFromToken(HttpServletRequest request){
        String token = jwtTokenProvider.resolveTokenToString(request);
        DecodedJWT decoded = jwtTokenProvider.decodeToken(token);
        return decoded.getClaim("id").asLong();
    }

}
