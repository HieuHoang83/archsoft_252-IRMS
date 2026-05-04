package com.irms.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UserResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private boolean active;
    private Set<RoleResponseDTO> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
