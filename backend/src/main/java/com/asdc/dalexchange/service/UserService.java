package com.asdc.dalexchange.service;

import com.asdc.dalexchange.dto.UserDTO;
import com.asdc.dalexchange.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {
    long newCustomers();
    double customersChange();
    LocalDateTime getCurrentDateTime();
    List<UserDTO> getAllUsers();
    Optional<UserDTO> viewUserDetails(long userId);
    UserDTO editUserDetails(long userId, UserDTO updatedUserDetails);
    UserDTO activateUser(long userId);
    UserDTO deactivateUser(long userId);
    void deleteUser(long userId);
    User registerUser(User user, MultipartFile profilePicture);
    boolean verifyUser(String email, String code);
    User getCurrentUser();
}
