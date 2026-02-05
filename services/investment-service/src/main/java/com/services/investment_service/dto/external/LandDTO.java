package com.services.investment_service.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * External DTO for Land data from Land Service
 * Maps to the response from land-service API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LandDTO {

    /**
     * Land ID
     */
    private Long id;

    /**
     * Owner ID (User who owns this land)
     */
    private Long ownerId;

    /**
     * Province where land is located
     */
    private String province;

    /**
     * District where land is located
     */
    private String district;

    /**
     * Sector where land is located
     */
    private String sector;

    /**
     * Area in square meters
     */
    private Double areaSqMeters;

    /**
     * Availability type (SALE, RENT, HARVEST_SHARE, etc.)
     */
    private String availabilityType;

    /**
     * Current status (AVAILABLE, SOLD, RENTED, etc.)
     */
    private String status;

    /**
     * Helper method to get full location as a formatted string
     * @return Formatted location string (e.g., "Remera, Gasabo, Kigali")
     */
    public String getFullLocation() {
        if (sector != null && district != null && province != null) {
            return String.format("%s, %s, %s", sector, district, province);
        } else if (district != null && province != null) {
            return String.format("%s, %s", district, province);
        } else if (province != null) {
            return province;
        }
        return "Unknown Location";
    }
}