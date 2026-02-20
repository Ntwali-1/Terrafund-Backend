package com.services.investment_service.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String email;

    private String fullName;

    private String phoneNumber;

    private String userType;

    private Boolean enabled;
}