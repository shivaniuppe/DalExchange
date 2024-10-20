package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.UserDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.model.VerificationCode;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.repository.VerificationCodeRepository;
import com.asdc.dalexchange.service.EmailService;
import com.asdc.dalexchange.service.UserService;
import com.asdc.dalexchange.util.AuthUtil;
import com.asdc.dalexchange.util.CloudinaryUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementation of the UserService interface.
 */
@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private VerificationCodeRepository verificationCodeRepository;
    private EmailService emailService;
    private Mapper<User, UserDTO> userMapper;
    private CloudinaryUtil cloudinaryUtil;
    final Map<String, User> temporaryUserStorage = new ConcurrentHashMap<>();

    /**
     * Returns the number of new customers who joined in the last 30 days.
     *
     * @return the number of new customers
     */
    public long newCustomers() {
        long count = userRepository.countUsersJoinedInLast30Days();
        log.info("Number of new customers in the last 30 days: {}", count);
        return count;
    }

    /**
     * Calculates the percentage change in the number of customers over the last 30 days.
     *
     * @return the percentage change in the number of customers
     */
    public double customersChange() {
        LocalDateTime now = getCurrentDateTime();
        LocalDateTime startOfCurrentPeriod = now.minusDays(30);
        LocalDateTime startOfPreviousPeriod = startOfCurrentPeriod.minusDays(30);

        Long totalCustomersLast30Days = userRepository.countUsersJoinedSince(startOfCurrentPeriod);
        Long totalCustomersPrevious30Days = userRepository.countUsersJoinedBetween(startOfPreviousPeriod, startOfCurrentPeriod);

        if (totalCustomersLast30Days == null) {
            totalCustomersLast30Days = 0L;
        }
        if (totalCustomersPrevious30Days == null) {
            totalCustomersPrevious30Days = 0L;
        }

        double percentageChange = calculatePercentageIncrease(totalCustomersLast30Days.doubleValue(), totalCustomersPrevious30Days.doubleValue());
        log.info("Percentage change in number of customers over the last 30 days: {}", percentageChange);
        return percentageChange;
    }

    /**
     * Returns the current date and time.
     *
     * @return the current date and time
     */
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Calculates the percentage increase between two values.
     *
     * @param current  the current value
     * @param previous the previous value
     * @return the percentage increase
     */
    private double calculatePercentageIncrease(Double current, Double previous) {
        if (previous == 0) {
            if (current > 0){
                return 100.0;
            }
            return  0.0;
        }
        return ((current - previous) / previous) * 100;
    }

    /**
     * Retrieves all users as a list of UserDTO objects.
     *
     * @return a list of UserDTO objects
     */
    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::mapTo)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the details of a user by user ID.
     *
     * @param userId the ID of the user
     * @return an Optional containing the UserDTO if found, or empty if not found
     */
    @Override
    public Optional<UserDTO> viewUserDetails(long userId) {
        log.info("Fetching details for user with ID: {}", userId);
        return userRepository.findById(userId)
                .map(userMapper::mapTo);
    }

    /**
     * Edits the details of an existing user.
     *
     * @param userId             the ID of the user to edit
     * @param updatedUserDetails the updated user details
     * @return the updated UserDTO
     */
    @Override
    public UserDTO editUserDetails(long userId, UserDTO updatedUserDetails) {
        log.info("Editing user details for user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updatedUserDetails.getEmail() != null) {
            user.setEmail(updatedUserDetails.getEmail());
        }
        if (updatedUserDetails.getPhoneNo() != null) {
            user.setPhoneNo(updatedUserDetails.getPhoneNo());
        }
        if (updatedUserDetails.getFullName() != null) {
            user.setFullName(updatedUserDetails.getFullName());
        }
        if (updatedUserDetails.getRole() != null) {
            user.setRole(updatedUserDetails.getRole());
        }
        if (updatedUserDetails.getActive() != null) {
            user.setActive(updatedUserDetails.getActive());
        }

        User updatedUser = userRepository.save(user);
        log.info("User details updated for user with ID: {}", userId);
        return userMapper.mapTo(updatedUser);
    }

    /**
     * Activates a user by user ID.
     *
     * @param userId the ID of the user to activate
     * @return the activated UserDTO
     */
    @Override
    public UserDTO activateUser(long userId) {
        log.info("Activating user with ID: {}", userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(true);
            User activatedUser = userRepository.save(user);
            log.info("User with ID: {} activated successfully", userId);
            return userMapper.mapTo(activatedUser);
        } else {
            log.error("User with ID: {} not found", userId);
            throw new RuntimeException("User not found");
        }
    }

    /**
     * Deactivates a user by user ID.
     *
     * @param userId the ID of the user to deactivate
     * @return the deactivated UserDTO
     */
    @Override
    public UserDTO deactivateUser(long userId) {
        log.info("Deactivating user with ID: {}", userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(false);
            User deactivatedUser = userRepository.save(user);
            log.info("User with ID: {} deactivated successfully", userId);
            return userMapper.mapTo(deactivatedUser);
        } else {
            log.error("User with ID: {} not found", userId);
            throw new RuntimeException("User not found");
        }
    }

    /**
     * Deletes a user by user ID.
     *
     * @param userId the ID of the user to delete
     */
    public void deleteUser(long userId) {
        log.info("Deleting user with ID: {}", userId);
        userRepository.deleteById(userId);
        log.info("User with ID: {} deleted successfully", userId);
    }

    /**
       * Verifies a user based on email and verification code.
       *
       * @param email the email of the user
       * @param code  the verification code
       * @return true if the verification is successful, false otherwise
       */
    @Override
    public boolean verifyUser(String email, String code) {
        log.info("Verifying user with email: {} and code: {}", email, code);
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findByEmailAndCode(email, code);
        boolean isValid = verificationCode.isPresent() && verificationCode.get().getExpiryDate().isAfter(LocalDateTime.now());

        if (isValid) {
            User user = temporaryUserStorage.remove(email);
            if (user != null) {
                userRepository.save(user);
                log.info("User verification successful and user data saved for email: {}", email);
            } else {
                log.warn("No user found in temporary storage for email: {}", email);
                return false;
            }
        } else {
            log.warn("User verification failed for email: {}", email);
        }

        return isValid;
    }

    /**
     * Generates a random 6-digit verification code.
     *
     * @return the generated verification code
     */
    private String generateVerificationCode() {
        Random random = new Random();
        String code = String.format("%06d", random.nextInt(999999));
        log.debug("Generated verification code: {}", code);
        return code;
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the currently authenticated user
     */
    public User getCurrentUser() {
        log.info("Fetching currently authenticated user");
        return AuthUtil.getCurrentUser(userRepository);
    }

    /**
     * Registers a new user and uploads their profile picture.
     *
     * @param user           the user to register
     * @param profilePicture the profile picture file
     * @return the registered user
     */
    @Override
    public User registerUser(User user, MultipartFile profilePicture) {
        log.info("Registering new user with email: {}", user.getEmail());

        String profilePictureURL = cloudinaryUtil.uploadImage(profilePicture);
        user.setProfilePicture(profilePictureURL);

        String verificationCode = generateVerificationCode();
        VerificationCode code = new VerificationCode();
        code.setEmail(user.getEmail());
        code.setCode(verificationCode);
        code.setExpiryDate(LocalDateTime.now().plusHours(1));
        verificationCodeRepository.save(code);

        String subject = "Verify your email";
        String text = "Your verification code is " + verificationCode;
        emailService.sendEmail(user.getEmail(), subject, text);

        temporaryUserStorage.put(user.getEmail(), user);

        log.info("User temporarily registered and verification email sent to: {}", user.getEmail());
        return user;
    }
}
