package com.example.jwtExample.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class JwtUtil {
    private final String KEY = "something";

    public String generateToken(UserDetails validUser){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,validUser.getUsername(),60*10);
    }

    private String createToken(Map<String, Object> claims, String username,long timeMinutes) {
        return Jwts.builder().setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*timeMinutes))
                .signWith(SignatureAlgorithm.HS256,KEY).compact();
    }
    public String generateRefreshToken(UserDetails validUser){
        return createToken(new HashMap<>(),validUser.getUsername(),60*24*7);
    }

    public boolean validateToken(String token, UserDetails user) {
        String userName = getUserName(token);
        return(userName.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public String getUserName(String token) {
        return getClaim(getAllClaims(token),t->t.getSubject());
    }

    private Boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
    public Date getExpiration(String token){
        return getClaim(getAllClaims(token),t->t.getExpiration());
    }



    public Claims getAllClaims(String token){
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }
    public <T> T getClaim(Claims claims, Function<Claims,T> resolver){
        return resolver.apply(claims);
    }
}

