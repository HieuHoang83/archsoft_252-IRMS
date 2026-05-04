package com.irms.admin.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private String email;
    private boolean active = true;
    private Set<String> roleNames;
}
