package com.services.user_service.service.impl;

import com.services.user_service.dto.*;
import com.services.user_service.entity.User;
import com.services.user_service.entity.UserRole;
import com.services.user_service.entity.VerificationToken;
import com.services.user_service.enums.Role;
import com.services.user_service.repository.UserRepository;
import com.services.user_service.repository.UserRoleRepository;
import com.services.user_service.repository.VerificationTokenRepository;
import com.services.user_service.security.JwtService;
import com.services.user_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setIsVerified(false);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(savedUser);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        verificationTokenRepository.save(verificationToken);

        // TODO: Send verification email
        // emailService.sendVerificationEmail(savedUser.getEmail(), token);

        return mapToUserResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        String token = jwtService.generateToken(user);
        user.setJwt(token);
        userRepository.save(user);

        Set<Role> roles = user.getRoles().stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet());

        return new LoginResponse(token, user.getId(), user.getEmail(),
                user.getFullName(), roles, user.getIsVerified());
    }

    @Override
    @Transactional
    public UserResponse selectRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user already has this role
        if (userRoleRepository.existsByUserIdAndRole(userId, role)) {
            throw new RuntimeException("User already has this role");
        }

        // Add new role
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        user.addRole(userRole);

        // Generate new JWT token with updated roles
        String newToken = jwtService.generateToken(user);
        user.setJwt(newToken);

        userRepository.save(user);

        // Refresh user to get updated roles
        user = userRepository.findById(userId).get();

        return mapToUserResponse(user);
    }

    @Override
    public UserResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }

        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        User updatedUser = userRepository.save(user);

        return mapToUserResponse(updatedUser);
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (verificationToken.isExpired()) {
            throw new RuntimeException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setIsVerified(true);
        userRepository.save(user);

        // Delete the used token
        verificationTokenRepository.delete(verificationToken);
    }

    @Override
    @Transactional
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getIsVerified()) {
            throw new RuntimeException("Email already verified");
        }

        // Delete old token if exists
        verificationTokenRepository.findByUserId(user.getId())
                .ifPresent(verificationTokenRepository::delete);

        // Create new token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        verificationTokenRepository.save(verificationToken);

        // TODO: Send verification email
        // emailService.sendVerificationEmail(user.getEmail(), token);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setIsVerified(user.getIsVerified());
        response.setJwtToken(user.getJwt());
        response.setIsActive(user.getIsActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        Set<Role> roles = user.getRoles().stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet());
        response.setRoles(roles);

        return response;
    }
}