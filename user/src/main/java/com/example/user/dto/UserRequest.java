package com.example.user.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String email;
    private String password;
    private String role; // e.g., "CUSTOMER", "AGENT", "ADMIN"
}