package com.services.user_service.dto;

import com.services.user_service.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleSelectionRequest {

    @NotNull(message = "Role is required")
    private Role role;
}