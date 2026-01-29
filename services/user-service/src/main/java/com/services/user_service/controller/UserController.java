package com.services.user_service.controller;

import com.services.user_service.dto.RoleSelectionRequest;
import com.services.user_service.dto.UpdateUserRequest;
import com.services.user_service.dto.UserResponse;
import com.services.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/select-role")
    public ResponseEntity<Map<String, Object>> selectRole(@Valid @RequestBody RoleSelectionRequest request) {
        Long userId = getCurrentUserId();
        UserResponse user = userService.selectRole(userId, request.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Role added successfully");
        response.put("user", user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Long userId = getCurrentUserId();
        UserResponse user = userService.getCurrentUser(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<Map<String, Object>> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {
        Long userId = getCurrentUserId();
        UserResponse user = userService.updateUser(userId, request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User updated successfully");
        response.put("user", user);

        return ResponseEntity.ok(response);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return 1L;
        }
        throw new RuntimeException("User not authenticated");
    }
}