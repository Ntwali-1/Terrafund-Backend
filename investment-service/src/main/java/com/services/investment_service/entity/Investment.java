package com.services.investment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "investments", indexes = {
        @Index(name = "idx_investor_id", columnList = "investor_id"),
        @Index(name = "idx_land_id", columnList = "land_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "investor_id", nullable = false)
    private Long investorId;

    @Column(name = "investor_name", nullable = false)
    private String investorName;

    @Column(name = "investor_email")
    private String investorEmail;

    @Column(name = "investor_phone")
    private String investorPhone;

    // Land Information
    @Column(name = "land_id", nullable = false)
    private Long landId;

    @Column(name = "land_owner_id", nullable = false)
    private Long landOwnerId;

    @Column(name = "land_location")
    private String landLocation;

    // Investment Details
    @Enumerated(EnumType.STRING)
    @Column(name = "investment_type", nullable = false, length = 50)
    private InvestmentType investmentType;

    @Column(name = "offer_amount", precision = 19, scale = 2)
    private BigDecimal offerAmount;

    @Column(name = "proposed_duration_months")
    private Integer proposedDurationMonths;

    @Column(name = "harvest_share_percentage", precision = 5, scale = 2)
    private BigDecimal harvestSharePercentage;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String terms;

    // Status and Tracking
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private InvestmentStatus status = InvestmentStatus.PENDING;

    @Column(name = "owner_response", columnDefinition = "TEXT")
    private String ownerResponse;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    // Contract Information
    @Column(name = "contract_signed")
    private Boolean contractSigned = false;

    @Column(name = "contract_url")
    private String contractUrl;

    @Column(name = "contract_signed_date")
    private LocalDateTime contractSignedDate;

    // Payment Information
    @Column(name = "payment_completed")
    private Boolean paymentCompleted = false;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "payment_reference")
    private String paymentReference;

    // Dates
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @Column(name = "rejected_date")
    private LocalDateTime rejectedDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Enums
    public enum InvestmentType {
        PURCHASE,
        LEASE,
        HARVEST_SHARE,
        PARTNERSHIP
    }

    public enum InvestmentStatus {
        PENDING,
        UNDER_REVIEW,
        APPROVED,
        REJECTED,
        CONTRACT_PENDING,
        CONTRACT_SIGNED,
        PAYMENT_PENDING,
        ACTIVE,
        COMPLETED,
        CANCELLED,
        DISPUTED
    }
}