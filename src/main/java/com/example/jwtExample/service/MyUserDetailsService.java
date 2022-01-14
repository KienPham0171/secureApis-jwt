package com.example.jwtExample.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.compareTo("kien")==0)
        return new User("kien","kien",new ArrayList<>());
        else{
            System.err.println("incorrect user");
            throw new UsernameNotFoundException("incorrect user");
        }
    }
}

