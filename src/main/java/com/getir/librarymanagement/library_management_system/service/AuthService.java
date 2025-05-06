package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.request.LoginRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.RegisterRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.LoginResponseDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.RegisterResponseDTO;

/**
 * Authentication service interface for handling user registration and login operations.
 */
public interface AuthService {

    /**
     * Registers a new user into the system.
     *
     * @param registerRequest DTO containing registration details
     * @return DTO with user info and JWT token
     */
    RegisterResponseDTO register(RegisterRequestDTO registerRequest);

    /**
     * Authenticates user with provided credentials.
     *
     * @param loginRequest DTO containing login credentials
     * @return DTO with JWT token and login status
     */
    LoginResponseDTO login(LoginRequestDTO loginRequest);
}