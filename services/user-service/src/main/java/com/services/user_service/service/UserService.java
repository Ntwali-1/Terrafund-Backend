package com.services.user_service.service;

import com.services.user_service.dto.*;
import com.services.user_service.enums.Role;

public interface UserService {

    UserResponse signup(SignupRequest request);

    LoginResponse login(LoginRequest request);

    UserResponse selectRole(Long userId, Role role);

    UserResponse getCurrentUser(Long userId);

    UserResponse updateUser(Long userId, UpdateUserRequest request);

    void verifyEmail(String token);

    void resendVerificationEmail(String email);
}