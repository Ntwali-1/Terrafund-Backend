package com.services.land_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.services.land_service.entity.Land.AvailabilityType;
import com.services.land_service.entity.Land.LandStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandResponse {

    private Long id;

    private Long ownerId;

    private String province;

    private String district;

    private String sector;

    private Double areaSqMeters;

    private AvailabilityType availabilityType;

    private LandStatus status;

    private List<String> imageUrls;

    private List<String> documentUrls;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}