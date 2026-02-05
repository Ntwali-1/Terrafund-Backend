package com.services.investment_service.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LandDTO {

    private Long id;

    private Long ownerId;

    private String province;

    private String district;

    private String sector;

    private Double areaSqMeters;

    private String availabilityType;

    private String status;

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