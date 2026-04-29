package com.irms.admin.service;

import com.irms.admin.domain.AuditLog;
import com.irms.admin.domain.User;
import com.irms.admin.dto.AuthRequest;
import com.irms.admin.dto.AuthResponse;
import com.irms.admin.dto.RegisterRequest;
import com.irms.admin.repository.AuditLogRepository;
import com.irms.admin.repository.UserRepository;
import com.irms.admin.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuditLogRepository auditLogRepository;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        
        var user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        
        userRepository.save(user);

        logAction("REGISTER_SUCCESS", "User", user.getId() != null ? user.getId().toString() : null, user.getUsername(), "New user registered");

        var jwtToken = jwtService.generateToken(new CustomUserDetails(user));
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
                
        var jwtToken = jwtService.generateToken(new CustomUserDetails(user));
        
        logAction("LOGIN_SUCCESS", "User", user.getId().toString(), user.getUsername(), "User logged in successfully");
        
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
    
    private void logAction(String action, String entityName, String entityId, String username, String details) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setPerformedBy(username);
        log.setDetails(details);
        auditLogRepository.save(log);
    }
}
