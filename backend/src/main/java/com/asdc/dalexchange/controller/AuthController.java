package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.enums.Role;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.model.VerificationRequest;
import com.asdc.dalexchange.service.UserService;
import com.asdc.dalexchange.util.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * REST controller for authentication-related operations.
 */
@RestController
@RequestMapping("/auth")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;

    /**
     * Handles user signup requests.
     *
     * @param username       the username
     * @param password       the password
     * @param email          the email address
     * @param firstName      the first name
     * @param phoneNumber    the phone number
     * @param profilePicture the profile picture
     * @param role           the role
     * @param bio            the bio
     * @return a ResponseEntity with the signup result
     */
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> signup(
            @RequestPart("username") String username,
            @RequestPart("password") String password,
            @RequestPart("email") String email,
            @RequestPart("firstName") String firstName,
            @RequestPart("phoneNumber") String phoneNumber,
            @RequestPart("profilePicture") MultipartFile profilePicture,
            @RequestPart("role") String role,
            @RequestPart("bio") String bio) {
        log.info("Signup attempt for email: {}", email);
        try {
            if (!email.endsWith("@dal.ca")) {
                log.warn("Signup attempt with invalid email domain: {}", email);
                return ResponseEntity.badRequest().body("Email must be a @dal.ca address");
            }
            String encodedPassword = passwordEncoder.encode(password);
            User user = new User();
            user.setUsername(username);
            user.setPassword(encodedPassword);
            user.setEmail(email);
            user.setFullName(firstName);
            user.setPhoneNo(phoneNumber);
            user.setRole(Role.valueOf(role));
            user.setBio(bio);
            user.setJoinedAt(LocalDateTime.now());
            user.setSellerRating(0.0);
            user.setActive(true);

            userService.registerUser(user, profilePicture);
            log.info("User temporarily registered: {}", email);
            return ResponseEntity.ok("User temporarily registered. Please check your email for the verification code.");
        } catch (Exception e) {
            log.error("Error registering user: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user.");
        }
    }

    /**
     * Handles user login requests.
     *
     * @param user the user credentials
     * @return a ResponseEntity with the login result
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        log.info("Login attempt for email: {}", user.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userDetails.getUsername());

            log.info("Login successful for email: {}", user.getEmail());
            return ResponseEntity.ok(Map.of("token", jwt));
        } catch (Exception e) {
            log.error("Login failed for email: {}", user.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    /**
     * Handles email verification requests.
     *
     * @param request the verification request
     * @return a ResponseEntity with the verification result
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerificationRequest request) {
        log.info("Received verification request: {}", request);
        try {
            log.info("Verifying user with email: {} and code: {}", request.getEmail(), request.getCode());
            boolean isVerified = userService.verifyUser(request.getEmail(), request.getCode());
            if (isVerified) {
                log.info("Verification successful for email: {}", request.getEmail());
                return ResponseEntity.ok("User verified successfully");
            } else {
                log.warn("Verification failed for email: {} with code: {}", request.getEmail(), request.getCode());
                return ResponseEntity.badRequest().body("Invalid verification code or code has expired");
            }
        } catch (Exception e) {
            log.error("Error verifying user: {}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying user.");
        }
    }

    /**
     * Retrieves the current authenticated user.
     *
     * @return a ResponseEntity with the current user information
     */
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        log.info("Fetching current user");
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            log.warn("No user is currently logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in");
        }
        log.info("Current user: {}", currentUser.getEmail());
        return ResponseEntity.ok(currentUser);
    }
}
