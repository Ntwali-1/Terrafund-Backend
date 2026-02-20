package com.services.investment_service.repository;

import com.services.investment_service.entity.Investment;
import com.services.investment_service.entity.Investment.InvestmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    // Find by investor
    Page<Investment> findByInvestorId(Long investorId, Pageable pageable);
    List<Investment> findByInvestorId(Long investorId);

    // Find by land owner
    Page<Investment> findByLandOwnerId(Long landOwnerId, Pageable pageable);

    // Find by land
    Page<Investment> findByLandId(Long landId, Pageable pageable);
    List<Investment> findByLandId(Long landId);

    // Find by status
    Page<Investment> findByStatus(InvestmentStatus status, Pageable pageable);

    // Find by investor and status
    Page<Investment> findByInvestorIdAndStatus(Long investorId, InvestmentStatus status, Pageable pageable);

    // Find by land owner and status
    Page<Investment> findByLandOwnerIdAndStatus(Long landOwnerId, InvestmentStatus status, Pageable pageable);

    // Check if investment exists
    boolean existsByIdAndInvestorId(Long id, Long investorId);
    boolean existsByIdAndLandOwnerId(Long id, Long landOwnerId);

    // Count queries for statistics
    long countByInvestorId(Long investorId);
    long countByInvestorIdAndStatus(Long investorId, InvestmentStatus status);
    long countByLandOwnerId(Long landOwnerId);
    long countByLandOwnerIdAndStatus(Long landOwnerId, InvestmentStatus status);

    // Sum of investments
    @Query("SELECT COALESCE(SUM(i.offerAmount), 0) FROM Investment i WHERE i.investorId = :investorId")
    BigDecimal sumOfferAmountByInvestorId(@Param("investorId") Long investorId);

    @Query("SELECT COALESCE(SUM(i.offerAmount), 0) FROM Investment i WHERE i.investorId = :investorId AND i.status = :status")
    BigDecimal sumOfferAmountByInvestorIdAndStatus(@Param("investorId") Long investorId, @Param("status") InvestmentStatus status);
}
