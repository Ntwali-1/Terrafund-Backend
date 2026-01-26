package com.services.land_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lands")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Land {

    //Enums
    public enum AvailabilityType {
        SALE,
        RENT,
        HARVEST_SHARE,
        SALE_OR_RENT,
        ALL
    }

    public enum LandStatus {
        AVAILABLE,
        PENDING,
        SOLD,
        RENTED,
        UNDER_CONTRACT,
        WITHDRAWN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "province")
    private String province;

    @Column(name = "district")
    private String district;

    @Column(name = "sector")
    private String sector;

    @Column(name = "area_sq_meters", nullable = false)
    private Double areaSqMeters;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability_type", nullable = false, length = 50)
    private AvailabilityType availabilityType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private LandStatus status = LandStatus.AVAILABLE;

    @ElementCollection
    @CollectionTable(name = "land_images", joinColumns = @JoinColumn(name = "land_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "land_documents", joinColumns = @JoinColumn(name = "land_id"))
    @Column(name = "document_url")
    private List<String> documentUrls = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

