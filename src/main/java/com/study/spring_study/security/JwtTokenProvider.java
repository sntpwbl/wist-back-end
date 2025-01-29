package com.study.spring_study.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.study.spring_study.dto.TokenDTO;
import com.study.spring_study.exception.InvalidJwtAuthenticationException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long expireLengthInMs = 3600000;

    private long refreshTokenExpireLengthInMs = expireLengthInMs * 5;
    
    @Autowired
    private final UserDetailsService userDetailsService;
    
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    Algorithm algorithm = null;
    
    @PostConstruct
    public void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }
    
    public TokenDTO createAccessTokenObject(Long id, String username, List<String> roles){
        Date tokenCreationTimestamp = new Date();
        Date tokenExpirationTime = new Date(tokenCreationTimestamp.getTime() + refreshTokenExpireLengthInMs);
        
        return new TokenDTO(
            username, 
            true, 
            tokenCreationTimestamp, 
            tokenExpirationTime, 
            getAccessToken(id, username, roles, tokenCreationTimestamp, tokenExpirationTime), 
            getRefreshToken(id, username, roles, tokenCreationTimestamp));
        }
        
    public String getAccessToken(Long id, String username, List<String> roles, Date tokenCreationTimestamp, Date tokenExpirationTime) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        
        return JWT.create()
            .withClaim("id", id)
            .withClaim("roles", roles)
            .withIssuedAt(tokenCreationTimestamp)
            .withExpiresAt(tokenExpirationTime)
            .withSubject(username)
            .withIssuer(issuerUrl)
            .sign(algorithm)
            .strip();
    }

    public String getRefreshToken(Long id, String username, List<String> roles, Date tokenCreationTimestamp) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        
        return JWT.create()
            .withClaim("id", id)
            .withClaim("roles", roles)
            .withIssuedAt(tokenCreationTimestamp)
            .withSubject(username)
            .withIssuer(issuerUrl)
            .sign(algorithm)
            .strip();
    }

    public Authentication getAuthentication(String token){
        DecodedJWT decodedToken = decodeToken(token);
        UserDetails user = userDetailsService.loadUserByUsername(decodedToken.getSubject());
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }
        
    public DecodedJWT decodeToken(String token) {
        return JWT.require(algorithm).build().verify(token);
    }

    public String resolveTokenToString(HttpServletRequest request){
        String authorizationHeaderValue = request.getHeader("Authorization");
        if(authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer ")){
            return authorizationHeaderValue.substring("Bearer ".length());
        } else return null;
    }

    public boolean validateToken(String token) throws InvalidJwtAuthenticationException {
        DecodedJWT decodedToken = decodeToken(token);
        try {
            if(decodedToken.getExpiresAt().before(new Date())){
                throw new InvalidJwtAuthenticationException("Expired token.");
            }
            return true;
        } catch (InvalidJwtAuthenticationException e) {
            return false;
        }
    }
}
