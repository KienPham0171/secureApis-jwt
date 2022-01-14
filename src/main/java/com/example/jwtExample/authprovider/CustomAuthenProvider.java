package com.example.jwtExample.authprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenProvider implements AuthenticationProvider {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passWordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String passWord = authentication.getCredentials().toString();

        UserDetails user = userDetailsService.loadUserByUsername(userName);
        if(passWordEncoder.matches(passWord,user.getPassword())){
            return new UsernamePasswordAuthenticationToken(
                    userName,
                    passWord,
                    user.getAuthorities()
            );
        }else{
            throw new BadCredentialsException("incorrect password!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
