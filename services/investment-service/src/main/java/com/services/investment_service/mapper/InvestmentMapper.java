package com.services.investment_service.mapper;

import com.services.investment_service.dto.response.InvestmentResponse;
import com.services.investment_service.dto.response.InvestmentSummaryResponse;
import com.services.investment_service.entity.Investment;
import org.springframework.stereotype.Component;

@Component
public class InvestmentMapper {

    public InvestmentResponse toResponse(Investment investment) {
        if (investment == null) {
            return null;
        }

        return InvestmentResponse.builder()
                .id(investment.getId())
                .investorId(investment.getInvestorId())
                .investorName(investment.getInvestorName())
                .investorEmail(investment.getInvestorEmail())
                .investorPhone(investment.getInvestorPhone())
                .landId(investment.getLandId())
                .landOwnerId(investment.getLandOwnerId())
                .landLocation(investment.getLandLocation())
                .investmentType(investment.getInvestmentType())
                .offerAmount(investment.getOfferAmount())
                .proposedDurationMonths(investment.getProposedDurationMonths())
                .harvestSharePercentage(investment.getHarvestSharePercentage())
                .message(investment.getMessage())
                .terms(investment.getTerms())
                .status(investment.getStatus())
                .ownerResponse(investment.getOwnerResponse())
                .rejectionReason(investment.getRejectionReason())
                .contractSigned(investment.getContractSigned())
                .contractUrl(investment.getContractUrl())
                .contractSignedDate(investment.getContractSignedDate())
                .paymentCompleted(investment.getPaymentCompleted())
                .paymentDate(investment.getPaymentDate())
                .paymentReference(investment.getPaymentReference())
                .startDate(investment.getStartDate())
                .endDate(investment.getEndDate())
                .approvedDate(investment.getApprovedDate())
                .rejectedDate(investment.getRejectedDate())
                .createdAt(investment.getCreatedAt())
                .updatedAt(investment.getUpdatedAt())
                .build();
    }

    public InvestmentSummaryResponse toSummaryResponse(Investment investment) {
        if (investment == null) {
            return null;
        }

        return InvestmentSummaryResponse.builder()
                .id(investment.getId())
                .landId(investment.getLandId())
                .landLocation(investment.getLandLocation())
                .investmentType(investment.getInvestmentType())
                .offerAmount(investment.getOfferAmount())
                .status(investment.getStatus())
                .createdAt(investment.getCreatedAt())
                .build();
    }
}