package com.services.investment_service.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * External DTO for User data from User Service
 * Maps to the response from user-service API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /**
     * User ID
     */
    private Long id;

    /**
     * User email address
     */
    private String email;

    /**
     * User's full name
     */
    private String fullName;

    /**
     * User's phone number
     */
    private String phoneNumber;

    /**
     * User type (INVESTOR, LAND_OWNER, ADMIN)
     */
    private String userType;

    /**
     * Account enabled status
     */
    private Boolean enabled;
}