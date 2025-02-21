package com.example.user.service;

import com.example.user.dto.LoginRequest;
import com.example.user.dto.LoginResponse;
import com.example.user.dto.UserRequest;
import com.example.user.dto.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRequest userRequest);
    LoginResponse loginUser(LoginRequest loginRequest);
    UserResponse getUserById(Long id);
}