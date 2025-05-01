package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.request.LoginRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.RegisterRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.LoginResponseDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.RegisterResponseDTO;

public interface AuthService {
    RegisterResponseDTO register(RegisterRequestDTO registerRequest);
    LoginResponseDTO login(LoginRequestDTO loginRequest);
} 