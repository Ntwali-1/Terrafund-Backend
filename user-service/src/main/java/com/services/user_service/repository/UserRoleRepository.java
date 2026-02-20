package com.services.user_service.repository;

import com.services.user_service.entity.UserRole;
import com.services.user_service.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByUserIdAndRole(Long userId, Role role);

    Boolean existsByUserIdAndRole(Long userId, Role role);
}