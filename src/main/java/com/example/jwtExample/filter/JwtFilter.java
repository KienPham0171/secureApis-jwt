package com.example.jwtExample.filter;

import com.example.jwtExample.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Filter;
@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String userName = null;
        String token = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            token = authorizationHeader.substring(7);
            userName = jwtUtil.getUserName(token);
        }
        if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails user = userDetailsService.loadUserByUsername(userName);
            if(jwtUtil.validateToken(token,user)){
                UsernamePasswordAuthenticationToken validUser =
                        new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                validUser.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(validUser);
            }
        }
        filterChain.doFilter(request,response);
    }
}
