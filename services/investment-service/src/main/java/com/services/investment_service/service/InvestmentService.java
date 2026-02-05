package com.services.investment_service.service;

import com.services.investment_service.dto.request.*;
import com.services.investment_service.dto.response.InvestmentResponse;
import com.services.investment_service.dto.response.InvestmentSummaryResponse;
import com.services.investment_service.dto.response.InvestorStatsResponse;
import com.services.investment_service.entity.Investment.InvestmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InvestmentService {


    InvestmentResponse createInvestment(CreateInvestmentRequest request, Long investorId, String investorName, String investorEmail, String investorPhone);

    InvestmentResponse getInvestmentById(Long id);

    Page<InvestmentSummaryResponse> getAllInvestments(Pageable pageable);

    Page<InvestmentSummaryResponse> getInvestmentsByInvestor(Long investorId, Pageable pageable);

    Page<InvestmentSummaryResponse> getInvestmentsByLandOwner(Long landOwnerId, Pageable pageable);

    Page<InvestmentSummaryResponse> getInvestmentsByLand(Long landId, Pageable pageable);

    Page<InvestmentSummaryResponse> getInvestmentsByStatus(InvestmentStatus status, Pageable pageable);

    InvestmentResponse updateInvestment(Long id, UpdateInvestmentRequest request, Long investorId);

    InvestmentResponse reviewInvestment(Long id, ReviewInvestmentRequest request, Long landOwnerId);

    InvestmentResponse signContract(Long id, SignContractRequest request, Long userId);

    InvestmentResponse submitPayment(Long id, PaymentRequest request, Long investorId);

    void cancelInvestment(Long id, Long investorId);

    InvestorStatsResponse getInvestorStats(Long investorId);
}