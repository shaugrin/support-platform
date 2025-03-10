package com.example.ticket.client;
import com.example.ticket.config.FeignClientConfiguration;
import com.example.ticket.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER", configuration = FeignClientConfiguration.class)
public interface UserServiceClient {
    @GetMapping("/api/users/{userId}")
    UserResponse getUserById(@PathVariable Long userId);
}
