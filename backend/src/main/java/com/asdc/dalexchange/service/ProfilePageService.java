package com.asdc.dalexchange.service;

import com.asdc.dalexchange.dto.*;

public interface ProfilePageService {
    EditProfileDTO editUserDetails(EditProfileDTO editProfileDTO);
    EditProfileDTO editGetUserDetails();
}