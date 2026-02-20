package com.services.user_service.dto;

import com.services.user_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String fullName;
    private Set<Role> roles;
    private Boolean isVerified;

    public LoginResponse(String token, Long id, String email, String fullName, Set<Role> roles, Boolean isVerified) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.roles = roles;
        this.isVerified = isVerified;
    }
}