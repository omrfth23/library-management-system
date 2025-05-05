package com.getir.librarymanagement.library_management_system.controller;

import com.getir.librarymanagement.library_management_system.model.dto.request.LoginRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.RegisterRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.LoginResponseDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.RegisterResponseDTO;
import com.getir.librarymanagement.library_management_system.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testRegister_success() {
        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("secure")
                .phone("1234567890")
                .build();

        RegisterResponseDTO responseDTO = new RegisterResponseDTO(1L, "john@example.com", "PATRON", "token123");
        when(authService.register(request)).thenReturn(responseDTO);

        ResponseEntity<RegisterResponseDTO> response = authController.register(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("john@example.com", response.getBody().getEmail());
    }

    @Test
    void testLogin_success() {
        LoginRequestDTO request = LoginRequestDTO.builder()
                .email("john@example.com")
                .password("secure")
                .build();

        LoginResponseDTO responseDTO = new LoginResponseDTO("token123", "Login successful");
        when(authService.login(request)).thenReturn(responseDTO);

        ResponseEntity<LoginResponseDTO> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("token123", response.getBody().getToken());
        assertEquals("Login successful", response.getBody().getMessage());
    }
}
