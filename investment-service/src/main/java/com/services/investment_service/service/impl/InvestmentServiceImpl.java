package com.services.investment_service.service.impl;

import com.services.investment_service.dto.request.*;
import com.services.investment_service.dto.response.InvestmentResponse;
import com.services.investment_service.dto.response.InvestmentSummaryResponse;
import com.services.investment_service.dto.response.InvestorStatsResponse;
import com.services.investment_service.entity.Investment;
import com.services.investment_service.entity.Investment.InvestmentStatus;
import com.services.investment_service.exception.InvestmentNotFoundException;
import com.services.investment_service.exception.UnauthorizedException;
import com.services.investment_service.mapper.InvestmentMapper;
import com.services.investment_service.repository.InvestmentRepository;
import com.services.investment_service.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvestmentServiceImpl implements InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final InvestmentMapper investmentMapper;
    private final com.services.investment_service.client.LandServiceClient landServiceClient;

    @Override
    @Transactional
    public InvestmentResponse createInvestment(CreateInvestmentRequest request, Long investorId,
                                               String investorName, String investorEmail, String investorPhone) {
        log.info("Creating investment for investor: {} on land: {}", investorId, request.getLandId());

        // Verify land exists and fetch details
        com.services.investment_service.dto.external.LandDTO land = landServiceClient.getLandById(request.getLandId());
        
        if (land == null) {
            throw new IllegalArgumentException("Land not found with ID: " + request.getLandId());
        }
        
        // Verify land is not already sold if purchasing
        // Note: Logic depends on exact business rules, assuming here we check basic status
        // if (!"AVAILABLE".equalsIgnoreCase(land.getStatus()) && !"PENDING".equalsIgnoreCase(land.getStatus())) {
        //    log.warn("Warning: Creating investment on land with status: {}", land.getStatus());
        // }

        Investment investment = new Investment();
        investment.setInvestorId(investorId);
        investment.setInvestorName(investorName);
        investment.setInvestorEmail(investorEmail);
        investment.setInvestorPhone(investorPhone);

        investment.setLandId(request.getLandId());
        investment.setLandOwnerId(land.getOwnerId()); 
        investment.setLandLocation(land.getFullLocation()); 

        investment.setInvestmentType(request.getInvestmentType());
        investment.setOfferAmount(request.getOfferAmount());
        investment.setProposedDurationMonths(request.getProposedDurationMonths());
        investment.setHarvestSharePercentage(request.getHarvestSharePercentage());
        investment.setMessage(request.getMessage());
        investment.setTerms(request.getTerms());

        investment.setStatus(InvestmentStatus.PENDING);

        Investment saved = investmentRepository.save(investment);
        log.info("Investment created with ID: {}", saved.getId());

        return investmentMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InvestmentResponse getInvestmentById(Long id) {
        Investment investment = investmentRepository.findById(id)
                .orElseThrow(() -> new InvestmentNotFoundException("Investment not found with ID: " + id));

        return investmentMapper.toResponse(investment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvestmentSummaryResponse> getAllInvestments(Pageable pageable) {
        return investmentRepository.findAll(pageable)
                .map(investmentMapper::toSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvestmentSummaryResponse> getInvestmentsByInvestor(Long investorId, Pageable pageable) {
        return investmentRepository.findByInvestorId(investorId, pageable)
                .map(investmentMapper::toSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvestmentSummaryResponse> getInvestmentsByLandOwner(Long landOwnerId, Pageable pageable) {
        return investmentRepository.findByLandOwnerId(landOwnerId, pageable)
                .map(investmentMapper::toSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvestmentSummaryResponse> getInvestmentsByLand(Long landId, Pageable pageable) {
        return investmentRepository.findByLandId(landId, pageable)
                .map(investmentMapper::toSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvestmentSummaryResponse> getInvestmentsByStatus(InvestmentStatus status, Pageable pageable) {
        return investmentRepository.findByStatus(status, pageable)
                .map(investmentMapper::toSummaryResponse);
    }

    @Override
    @Transactional
    public InvestmentResponse updateInvestment(Long id, UpdateInvestmentRequest request, Long investorId) {
        Investment investment = investmentRepository.findById(id)
                .orElseThrow(() -> new InvestmentNotFoundException("Investment not found with ID: " + id));

        // Check ownership
        if (!investment.getInvestorId().equals(investorId)) {
            throw new UnauthorizedException("You can only update your own investments");
        }

        // Only allow updates for PENDING investments
        if (investment.getStatus() != InvestmentStatus.PENDING) {
            throw new IllegalStateException("Can only update pending investments");
        }

        // Update fields
        if (request.getOfferAmount() != null) {
            investment.setOfferAmount(request.getOfferAmount());
        }
        if (request.getProposedDurationMonths() != null) {
            investment.setProposedDurationMonths(request.getProposedDurationMonths());
        }
        if (request.getHarvestSharePercentage() != null) {
            investment.setHarvestSharePercentage(request.getHarvestSharePercentage());
        }
        if (request.getMessage() != null) {
            investment.setMessage(request.getMessage());
        }
        if (request.getTerms() != null) {
            investment.setTerms(request.getTerms());
        }

        Investment updated = investmentRepository.save(investment);
        return investmentMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public InvestmentResponse reviewInvestment(Long id, ReviewInvestmentRequest request, Long landOwnerId) {
        Investment investment = investmentRepository.findById(id)
                .orElseThrow(() -> new InvestmentNotFoundException("Investment not found with ID: " + id));

        // Check if this is the land owner
        if (!investment.getLandOwnerId().equals(landOwnerId)) {
            throw new UnauthorizedException("You can only review investments for your own lands");
        }

        // Update status
        investment.setStatus(request.getStatus());
        investment.setOwnerResponse(request.getResponse());

        if (request.getStatus() == InvestmentStatus.APPROVED) {
            investment.setApprovedDate(LocalDateTime.now());
            investment.setStatus(InvestmentStatus.CONTRACT_PENDING);
        } else if (request.getStatus() == InvestmentStatus.REJECTED) {
            investment.setRejectionReason(request.getRejectionReason());
            investment.setRejectedDate(LocalDateTime.now());
        }

        Investment updated = investmentRepository.save(investment);
        log.info("Investment {} reviewed: {}", id, request.getStatus());

        return investmentMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public InvestmentResponse signContract(Long id, SignContractRequest request, Long userId) {
        Investment investment = investmentRepository.findById(id)
                .orElseThrow(() -> new InvestmentNotFoundException("Investment not found with ID: " + id));

        // Verify user is either investor or land owner
        if (!investment.getInvestorId().equals(userId) && !investment.getLandOwnerId().equals(userId)) {
            throw new UnauthorizedException("Only investor or land owner can sign contract");
        }

        if (!request.getAgreedToTerms()) {
            throw new IllegalArgumentException("Must agree to terms to sign contract");
        }

        investment.setContractUrl(request.getContractUrl());
        investment.setContractSigned(true);
        investment.setContractSignedDate(LocalDateTime.now());
        investment.setStatus(InvestmentStatus.PAYMENT_PENDING);

        Investment updated = investmentRepository.save(investment);
        return investmentMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public InvestmentResponse submitPayment(Long id, PaymentRequest request, Long investorId) {
        Investment investment = investmentRepository.findById(id)
                .orElseThrow(() -> new InvestmentNotFoundException("Investment not found with ID: " + id));

        if (!investment.getInvestorId().equals(investorId)) {
            throw new UnauthorizedException("Only the investor can submit payment");
        }

        investment.setPaymentReference(request.getPaymentReference());
        investment.setPaymentCompleted(true);
        investment.setPaymentDate(LocalDateTime.now());
        investment.setStatus(InvestmentStatus.ACTIVE);
        investment.setStartDate(LocalDateTime.now());

        // Calculate end date if duration is provided
        if (investment.getProposedDurationMonths() != null) {
            investment.setEndDate(LocalDateTime.now().plusMonths(investment.getProposedDurationMonths()));
        }

        Investment updated = investmentRepository.save(investment);
        return investmentMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void cancelInvestment(Long id, Long investorId) {
        Investment investment = investmentRepository.findById(id)
                .orElseThrow(() -> new InvestmentNotFoundException("Investment not found with ID: " + id));

        if (!investment.getInvestorId().equals(investorId)) {
            throw new UnauthorizedException("You can only cancel your own investments");
        }

        // Only allow cancellation for certain statuses
        if (investment.getStatus() == InvestmentStatus.ACTIVE ||
                investment.getStatus() == InvestmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel active or completed investments");
        }

        investment.setStatus(InvestmentStatus.CANCELLED);
        investmentRepository.save(investment);
    }

    @Override
    @Transactional(readOnly = true)
    public InvestorStatsResponse getInvestorStats(Long investorId) {
        long total = investmentRepository.countByInvestorId(investorId);
        long pending = investmentRepository.countByInvestorIdAndStatus(investorId, InvestmentStatus.PENDING);
        long approved = investmentRepository.countByInvestorIdAndStatus(investorId, InvestmentStatus.APPROVED);
        long rejected = investmentRepository.countByInvestorIdAndStatus(investorId, InvestmentStatus.REJECTED);
        long active = investmentRepository.countByInvestorIdAndStatus(investorId, InvestmentStatus.ACTIVE);

        BigDecimal totalAmount = investmentRepository.sumOfferAmountByInvestorId(investorId);

        return InvestorStatsResponse.builder()
                .investorId(investorId)
                .totalInvestments(total)
                .pendingInvestments(pending)
                .approvedInvestments(approved)
                .rejectedInvestments(rejected)
                .activeInvestments(active)
                .totalInvestedAmount(totalAmount)
                .build();
    }
}