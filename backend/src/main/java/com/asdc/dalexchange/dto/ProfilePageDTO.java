package com.asdc.dalexchange.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePageDTO {
    private String profilePicture;
    private String username;
    private String bio;
}

