package com.services.investment_service.service;

import com.services.investment_service.dto.request.*;
import com.services.investment_service.dto.response.InvestmentResponse;
import com.services.investment_service.dto.response.InvestmentSummaryResponse;
import com.services.investment_service.dto.response.InvestorStatsResponse;
import com.services.investment_service.entity.Investment.InvestmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InvestmentService {

    /**
     * Create a new investment (INVESTOR only)
     */
    InvestmentResponse createInvestment(CreateInvestmentRequest request, Long investorId, String investorName, String investorEmail, String investorPhone);

    /**
     * Get investment by ID
     */
    InvestmentResponse getInvestmentById(Long id);

    /**
     * Get all investments with pagination
     */
    Page<InvestmentSummaryResponse> getAllInvestments(Pageable pageable);

    /**
     * Get investments by investor
     */
    Page<InvestmentSummaryResponse> getInvestmentsByInvestor(Long investorId, Pageable pageable);

    /**
     * Get investments by land owner
     */
    Page<InvestmentSummaryResponse> getInvestmentsByLandOwner(Long landOwnerId, Pageable pageable);

    /**
     * Get investments by land
     */
    Page<InvestmentSummaryResponse> getInvestmentsByLand(Long landId, Pageable pageable);

    /**
     * Get investments by status
     */
    Page<InvestmentSummaryResponse> getInvestmentsByStatus(InvestmentStatus status, Pageable pageable);

    /**
     * Update investment (INVESTOR only, before approval)
     */
    InvestmentResponse updateInvestment(Long id, UpdateInvestmentRequest request, Long investorId);

    /**
     * Review investment - Approve or Reject (LAND_OWNER only)
     */
    InvestmentResponse reviewInvestment(Long id, ReviewInvestmentRequest request, Long landOwnerId);

    /**
     * Sign contract (INVESTOR or LAND_OWNER)
     */
    InvestmentResponse signContract(Long id, SignContractRequest request, Long userId);

    /**
     * Submit payment (INVESTOR only)
     */
    InvestmentResponse submitPayment(Long id, PaymentRequest request, Long investorId);

    /**
     * Cancel investment (INVESTOR only)
     */
    void cancelInvestment(Long id, Long investorId);

    /**
     * Get investor statistics
     */
    InvestorStatsResponse getInvestorStats(Long investorId);
}