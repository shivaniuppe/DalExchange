package com.asdc.dalexchange.repository;

import com.asdc.dalexchange.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for VerificationCode entity.
 */
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    /**
     * Find a VerificationCode by email and code.
     *
     * @param email the email address
     * @param code  the verification code
     * @return an Optional containing the VerificationCode if found
     */
    Optional<VerificationCode> findByEmailAndCode(String email, String code);
}
