package com.services.investment_service.dto.request;

import com.services.investment_service.entity.Investment;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewInvestmentRequest {

    @NotNull(message = "Status is required")
    private Investment.InvestmentStatus status; // APPROVED or REJECTED

    @Size(max = 1000, message = "Response cannot exceed 1000 characters")
    private String response;

    @Size(max = 500, message = "Rejection reason cannot exceed 500 characters")
    private String rejectionReason;
}
