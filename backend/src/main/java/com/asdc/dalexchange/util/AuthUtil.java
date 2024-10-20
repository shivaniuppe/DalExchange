package com.asdc.dalexchange.util;

import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for authentication-related operations.
 */
public class AuthUtil {

    /**
     * Retrieves the current authenticated user from the UserRepository.
     *
     * @param userRepository the UserRepository to use for fetching the user
     * @return the current authenticated User, or null if not found
     */
    public static User getCurrentUser(UserRepository userRepository) {
        return userRepository.findByEmail(getEmail()).orElse(null);
    }

    /**
     * Retrieves the ID of the current authenticated user.
     *
     * @param userRepository the UserRepository to use for fetching the user
     * @return the ID of the current authenticated user, or null if not found
     */
    public static Long getCurrentUserId(UserRepository userRepository) {
        User currentUser = getCurrentUser(userRepository);
        if(currentUser == null) {
            return null;
        }
        return currentUser.getUserId();
    }

    /**
     * Retrieves the email of the current authenticated user from the SecurityContext.
     *
     * @return the email of the current authenticated user, or null if not authenticated
     */
    private static String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String email;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return email;
    }
}
