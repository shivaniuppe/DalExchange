package com.asdc.dalexchange.repository;

import com.asdc.dalexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository interface for User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their user ID.
     *
     * @param userId the user ID
     * @return the user with the given ID
     */
    User findByUserId(Long userId);

    /**
     * Find a user by their email.
     *
     * @param email the email address
     * @return an Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Count the number of users who joined in the last 30 days.
     *
     * @return the count of users
     */
    @Query(value = "SELECT COUNT(*) FROM user WHERE joined_at >= CURDATE() - INTERVAL 30 DAY", nativeQuery = true)
    Long countUsersJoinedInLast30Days();

    /**
     * Count the number of users who joined since a given date.
     *
     * @param startDate the start date
     * @return the count of users
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.joinedAt >= :startDate")
    Long countUsersJoinedSince(LocalDateTime startDate);

    /**
     * Count the number of users who joined between two dates.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return the count of users
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.joinedAt >= :startDate AND u.joinedAt < :endDate")
    Long countUsersJoinedBetween(LocalDateTime startDate, LocalDateTime endDate);
}
