package com.example.jwtExample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String userName;
}
