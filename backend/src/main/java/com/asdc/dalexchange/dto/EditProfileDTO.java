package com.asdc.dalexchange.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditProfileDTO {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String phoneNo;
    private String fullName;
    private String profilePicture;
    private String bio;
}

