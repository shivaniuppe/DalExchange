package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.UserDTO;
import com.asdc.dalexchange.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * Controller class to handle admin user-related requests.
 */
@RestController
@RequestMapping("/admin/users")
@Slf4j
@AllArgsConstructor
public class AdminUserController {

    private final UserService userService;

    /**
     * Retrieves all users.
     *
     * @return List of UserDTO containing all users.
     */
    @GetMapping("/all")
    public List<UserDTO> getUsers() {
        log.info("Fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        log.info("All users fetched successfully");
        return users;
    }

    /**
     * Retrieves user details by ID.
     *
     * @param id the ID of the user to retrieve.
     * @return Optional containing UserDTO of the specified user.
     */
    @GetMapping("/{id}")
    public Optional<UserDTO> viewUserDetails(@PathVariable Long id) {
        log.info("Fetching user details for ID: {}", id);
        Optional<UserDTO> user = userService.viewUserDetails(id);
        log.info("User details for ID: {} fetched successfully", id);
        return user;
    }

    /**
     * Edits user details by ID.
     *
     * @param id the ID of the user to edit.
     * @param userDTO the user details to update.
     * @return UserDTO containing updated user details.
     */
    @PutMapping("/{id}")
    public UserDTO editUserDetails(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        log.info("Editing user details for ID: {}", id);
        UserDTO updatedUser = userService.editUserDetails(id, userDTO);
        log.info("User details for ID: {} updated successfully", id);
        return updatedUser;
    }

    /**
     * Activates user account by ID.
     *
     * @param id the ID of the user to activate.
     * @return UserDTO containing updated user details.
     */
    @PutMapping("/{id}/activate")
    public UserDTO activateUser(@PathVariable Long id) {
        log.info("Activating user account for ID: {}", id);
        UserDTO activatedUser = userService.activateUser(id);
        log.info("User account for ID: {} activated successfully", id);
        return activatedUser;
    }

    /**
     * Deactivates user account by ID.
     *
     * @param id the ID of the user to deactivate.
     * @return UserDTO containing updated user details.
     */
    @PutMapping("/{id}/deactivate")
    public UserDTO deactivateUser(@PathVariable Long id) {
        log.info("Deactivating user account for ID: {}", id);
        UserDTO deactivatedUser = userService.deactivateUser(id);
        log.info("User account for ID: {} deactivated successfully", id);
        return deactivatedUser;
    }

    /**
     * Deletes user account by ID.
     *
     * @param id the ID of the user to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Deleting user account for ID: {}", id);
        userService.deleteUser(id);
        log.info("User account for ID: {} deleted successfully", id);
    }
}
