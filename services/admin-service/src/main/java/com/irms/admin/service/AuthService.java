package com.irms.admin.service;

import com.irms.admin.dto.AuthRequest;
import com.irms.admin.dto.AuthResponse;
import com.irms.admin.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
}
