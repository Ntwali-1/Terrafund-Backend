package com.services.investment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.services.investment_service.entity.Investment.InvestmentStatus;
import com.services.investment_service.entity.Investment.InvestmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentResponse {

    private Long id;

    // Investor Info
    private Long investorId;
    private String investorName;
    private String investorEmail;
    private String investorPhone;

    // Land Info
    private Long landId;
    private Long landOwnerId;
    private String landLocation;

    // Investment Details
    private InvestmentType investmentType;
    private BigDecimal offerAmount;
    private Integer proposedDurationMonths;
    private BigDecimal harvestSharePercentage;
    private String message;
    private String terms;

    // Status
    private InvestmentStatus status;
    private String ownerResponse;
    private String rejectionReason;

    // Contract
    private Boolean contractSigned;
    private String contractUrl;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime contractSignedDate;

    // Payment
    private Boolean paymentCompleted;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime paymentDate;

    private String paymentReference;

    // Dates
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime approvedDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime rejectedDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}