package com.asdc.dalexchange.service.impl;
import com.asdc.dalexchange.dto.EditProfileDTO;
import com.asdc.dalexchange.mappers.impl.EditProfileMapperImpl;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfilePageServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EditProfileMapperImpl editProfileMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProfilePageServiceImpl profilePageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /*@Test
    void testEditUserDetails_Success() {

        Long userId = AuthUtil.getCurrentUserId(userRepository);
        EditProfileDTO editProfileDTO = createEditProfileDTO();
        User user = createUser();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(modelMapper.map(editProfileDTO, User.class)).thenReturn(user);

        when(userRepository.save(user)).thenReturn(user);

        when(editProfileMapper.mapTo(user)).thenReturn(editProfileDTO);

        EditProfileDTO updatedProfile = profilePageService.editUserDetails(editProfileDTO);

        assertNotNull(updatedProfile);
        assertEquals(editProfileDTO.getUsername(), updatedProfile.getUsername());
        assertEquals(editProfileDTO.getEmail(), updatedProfile.getEmail());
    }*/

    @Test
    void testEditUserDetails_Success() {
        Long userId = 1L;
        String newPassword = "newPassword";
        String encodedPassword = "encodedPassword";

        EditProfileDTO editProfileDTO = createEditProfileDTO();
        editProfileDTO.setPassword(newPassword);
        User user = createUser();
        user.setPassword(encodedPassword);

        try (MockedStatic<AuthUtil> authUtilMock = mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

            when(modelMapper.map(editProfileDTO, User.class)).thenReturn(user);

            when(userRepository.save(user)).thenReturn(user);

            when(editProfileMapper.mapTo(user)).thenReturn(editProfileDTO);

            EditProfileDTO updatedProfile = profilePageService.editUserDetails(editProfileDTO);

            assertNotNull(updatedProfile);
            assertEquals(editProfileDTO.getUsername(), updatedProfile.getUsername());
            assertEquals(editProfileDTO.getEmail(), updatedProfile.getEmail());
            assertEquals(encodedPassword, updatedProfile.getPassword());
        }
    }



    @Test
    void testEditUserDetails_UserNotFound() {
        Long userId = 1L;
        EditProfileDTO editProfileDTO = createEditProfileDTO();


        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            profilePageService.editUserDetails(editProfileDTO);
        });
    }

    @Test
    void testGetUserDetails_Success() {

        Long userId = 1L;
        User user = createUser();
        EditProfileDTO editProfileDTO = createEditProfileDTO();

        try (MockedStatic<AuthUtil> authUtilMock = mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            when(modelMapper.map(user, EditProfileDTO.class)).thenReturn(editProfileDTO);

            EditProfileDTO userDetails = profilePageService.editGetUserDetails();

            assertNotNull(userDetails);
            assertEquals(user.getUsername(), userDetails.getUsername());
            assertEquals(user.getEmail(), userDetails.getEmail());

        }
    }

    private User createUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        return user;
    }

    private EditProfileDTO createEditProfileDTO() {
        EditProfileDTO dto = new EditProfileDTO();
        dto.setUsername("testUser");
        dto.setEmail("test@example.com");
        return dto;
    }

    @Test
    void testGetUserDetails_UserNotFound() {
        Long userId = 1L;

        try (MockedStatic<AuthUtil> authUtilMock = mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
                profilePageService.editGetUserDetails();
            });

            assertEquals("User not found", thrownException.getMessage());
        }
    }

}





