package com.services.user_service.controller;

import com.services.user_service.dto.LoginRequest;
import com.services.user_service.dto.LoginResponse;
import com.services.user_service.dto.SignupRequest;
import com.services.user_service.dto.UserResponse;
import com.services.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody SignupRequest request) {
        UserResponse user = userService.signup(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully. Please check your email to verify your account.");
        response.put("user", user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Email verified successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Map<String, String>> resendVerificationEmail(@RequestParam String email) {
        userService.resendVerificationEmail(email);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Verification email sent successfully");

        return ResponseEntity.ok(response);
    }
}