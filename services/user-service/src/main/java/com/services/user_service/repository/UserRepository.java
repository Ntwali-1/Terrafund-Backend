package com.services.user_service.repository;

import com.services.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.roles
        WHERE u.id = :id
    """)
    Optional<User> findByIdWithRoles(Long id);
}