package com.example.notification.client;

import com.example.notification.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER")
public interface UserServiceClient {
    @GetMapping("/api/users/{userId}")
    UserResponse getUserById(@PathVariable Long userId);
}