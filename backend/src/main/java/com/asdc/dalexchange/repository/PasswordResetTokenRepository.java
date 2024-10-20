package com.asdc.dalexchange.repository;

import com.asdc.dalexchange.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for PasswordResetToken entity.
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    /**
     * Find a PasswordResetToken by its token.
     *
     * @param token the token string
     * @return an Optional containing the PasswordResetToken if found
     */
    Optional<PasswordResetToken> findByToken(String token);
}
