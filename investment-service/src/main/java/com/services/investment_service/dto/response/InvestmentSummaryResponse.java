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
public class InvestmentSummaryResponse {

    private Long id;
    private Long landId;
    private String landLocation;
    private InvestmentType investmentType;
    private BigDecimal offerAmount;
    private InvestmentStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
