package com.services.investment_service.controller;

import com.services.investment_service.dto.request.*;
import com.services.investment_service.dto.response.InvestmentResponse;
import com.services.investment_service.dto.response.InvestmentSummaryResponse;
import com.services.investment_service.dto.response.InvestorStatsResponse;
import com.services.investment_service.entity.Investment.InvestmentStatus;
import com.services.investment_service.service.InvestmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/investments")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    /**
     * Create investment (INVESTOR only)
     * POST /api/investments
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_INVESTOR')")
    public ResponseEntity<InvestmentResponse> createInvestment(
            @Valid @RequestBody CreateInvestmentRequest request,
            HttpServletRequest httpRequest) {

        Long investorId = (Long) httpRequest.getAttribute("userId");
        String email = (String) httpRequest.getAttribute("email");

        // You can fetch full user details from User Service if needed
        String investorName = email; // Placeholder
        String investorPhone = "";   // Placeholder

        InvestmentResponse response = investmentService.createInvestment(
                request, investorId, investorName, email, investorPhone);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get investment by ID
     * GET /api/investments/{id}
     * Accessible by INVESTOR (own investments) or LAND_OWNER (their land investments)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_INVESTOR', 'ROLE_LAND_OWNER', 'ROLE_ADMIN')")
    public ResponseEntity<InvestmentResponse> getInvestmentById(@PathVariable Long id) {
        InvestmentResponse response = investmentService.getInvestmentById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all investments (ADMIN only)
     * GET /api/investments
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<InvestmentSummaryResponse>> getAllInvestments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InvestmentSummaryResponse> response = investmentService.getAllInvestments(pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Get my investments (current investor)
     * GET /api/investments/my-investments
     */
    @GetMapping("/my-investments")
    @PreAuthorize("hasRole('ROLE_INVESTOR')")
    public ResponseEntity<Page<InvestmentSummaryResponse>> getMyInvestments(
            HttpServletRequest httpRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long investorId = (Long) httpRequest.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<InvestmentSummaryResponse> response = investmentService.getInvestmentsByInvestor(
                investorId, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Get investments received (for land owners)
     * GET /api/investments/received
     */
    @GetMapping("/received")
    @PreAuthorize("hasRole('ROLE_LAND_OWNER')")
    public ResponseEntity<Page<InvestmentSummaryResponse>> getReceivedInvestments(
            HttpServletRequest httpRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long landOwnerId = (Long) httpRequest.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<InvestmentSummaryResponse> response = investmentService.getInvestmentsByLandOwner(
                landOwnerId, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Get investments by land
     * GET /api/investments/land/{landId}
     */
    @GetMapping("/land/{landId}")
    public ResponseEntity<Page<InvestmentSummaryResponse>> getInvestmentsByLand(
            @PathVariable Long landId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<InvestmentSummaryResponse> response = investmentService.getInvestmentsByLand(
                landId, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Update investment (INVESTOR only, before approval)
     * PUT /api/investments/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_INVESTOR')")
    public ResponseEntity<InvestmentResponse> updateInvestment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInvestmentRequest request,
            HttpServletRequest httpRequest) {

        Long investorId = (Long) httpRequest.getAttribute("userId");
        InvestmentResponse response = investmentService.updateInvestment(id, request, investorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Review investment - Approve/Reject (LAND_OWNER only)
     * POST /api/investments/{id}/review
     */
    @PostMapping("/{id}/review")
    @PreAuthorize("hasRole('ROLE_LAND_OWNER')")
    public ResponseEntity<InvestmentResponse> reviewInvestment(
            @PathVariable Long id,
            @Valid @RequestBody ReviewInvestmentRequest request,
            HttpServletRequest httpRequest) {

        Long landOwnerId = (Long) httpRequest.getAttribute("userId");
        InvestmentResponse response = investmentService.reviewInvestment(id, request, landOwnerId);

        return ResponseEntity.ok(response);
    }

    /**
     * Sign contract (INVESTOR or LAND_OWNER)
     * POST /api/investments/{id}/sign-contract
     */
    @PostMapping("/{id}/sign-contract")
    @PreAuthorize("hasAnyRole('ROLE_INVESTOR', 'ROLE_LAND_OWNER')")
    public ResponseEntity<InvestmentResponse> signContract(
            @PathVariable Long id,
            @Valid @RequestBody SignContractRequest request,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        InvestmentResponse response = investmentService.signContract(id, request, userId);

        return ResponseEntity.ok(response);
    }

    /**
     * Submit payment (INVESTOR only)
     * POST /api/investments/{id}/payment
     */
    @PostMapping("/{id}/payment")
    @PreAuthorize("hasRole('ROLE_INVESTOR')")
    public ResponseEntity<InvestmentResponse> submitPayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request,
            HttpServletRequest httpRequest) {

        Long investorId = (Long) httpRequest.getAttribute("userId");
        InvestmentResponse response = investmentService.submitPayment(id, request, investorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Cancel investment (INVESTOR only)
     * DELETE /api/investments/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_INVESTOR')")
    public ResponseEntity<Void> cancelInvestment(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        Long investorId = (Long) httpRequest.getAttribute("userId");
        investmentService.cancelInvestment(id, investorId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Get investor statistics
     * GET /api/investments/stats
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ROLE_INVESTOR')")
    public ResponseEntity<InvestorStatsResponse> getInvestorStats(
            HttpServletRequest httpRequest) {

        Long investorId = (Long) httpRequest.getAttribute("userId");
        InvestorStatsResponse response = investmentService.getInvestorStats(investorId);

        return ResponseEntity.ok(response);
    }
}