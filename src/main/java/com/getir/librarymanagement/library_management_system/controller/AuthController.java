package com.getir.librarymanagement.library_management_system.controller;

import com.getir.librarymanagement.library_management_system.model.dto.request.LoginRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.RegisterRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.LoginResponseDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.RegisterResponseDTO;
import com.getir.librarymanagement.library_management_system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user as a PATRON by default. Requires name, email, phone, and password.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Validation error or duplicate email/phone"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        RegisterResponseDTO response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Login with email and password",
            description = "Authenticates a user and returns a JWT token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
} 