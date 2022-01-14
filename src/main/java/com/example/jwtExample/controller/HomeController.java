package com.example.jwtExample.controller;

import com.example.jwtExample.model.AuthRequest;
import com.example.jwtExample.model.AuthResponse;
import com.example.jwtExample.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    AuthenticationManager authManager;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/")
    public String home(){
        return "hello";
    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authUser) throws Exception {
        System.err.println(authUser.toString());
        try{
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authUser.getUserName(),authUser.getPassWord())

            );
        }catch (BadCredentialsException e){
            throw new Exception("Incorrect username",e);
        }
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(authUser.getUserName());
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        String userName = userDetails.getUsername();
        return ResponseEntity.ok(new AuthResponse(accessToken,refreshToken,userName));

    }
}
