package com.asdc.dalexchange.dto;

import com.asdc.dalexchange.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNo;
    private Role role;
    private Boolean active;
}
