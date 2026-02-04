package com.services.investment_service.dto.request;

import java.math.BigDecimal;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInvestmentRequest {

    @DecimalMin(value = "0.01", message = "Offer amount must be greater than 0")
    private BigDecimal offerAmount;

    @Min(value = 1, message = "Duration must be at least 1 month")
    private Integer proposedDurationMonths;

    @DecimalMin(value = "0.0", message = "Harvest share must be at least 0%")
    @DecimalMax(value = "100.0", message = "Harvest share cannot exceed 100%")
    private BigDecimal harvestSharePercentage;

    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    private String message;

    @Size(max = 2000, message = "Terms cannot exceed 2000 characters")
    private String terms;
}
