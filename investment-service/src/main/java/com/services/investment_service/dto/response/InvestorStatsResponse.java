package com.services.investment_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestorStatsResponse {

    private Long investorId;
    private Long totalInvestments;
    private Long pendingInvestments;
    private Long approvedInvestments;
    private Long rejectedInvestments;
    private Long activeInvestments;
    private BigDecimal totalInvestedAmount;
}
