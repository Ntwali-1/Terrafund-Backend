package com.services.investment_service.dto.request;

import com.services.investment_service.entity.Investment;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInvestmentRequest {

    @NotNull(message = "Land ID is required")
    private Long landId;

    @NotNull(message = "Investment type is required")
    private Investment.InvestmentType investmentType;

    @NotNull(message = "Offer amount is required")
    @DecimalMin(value = "0.01", message = "Offer amount must be greater than 0")
    private BigDecimal offerAmount;

    @Min(value = 1, message = "Duration must be at least 1 month")
    @Max(value = 600, message = "Duration cannot exceed 240 months (20 years)")
    private Integer proposedDurationMonths;

    @DecimalMin(value = "0.0", message = "Harvest share must be at least 0%")
    @DecimalMax(value = "100.0", message = "Harvest share cannot exceed 100%")
    private BigDecimal harvestSharePercentage;

    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    private String message;

    @Size(max = 2000, message = "Terms cannot exceed 2000 characters")
    private String terms;
}
