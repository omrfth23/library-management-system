package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.enums.Role;
import com.getir.librarymanagement.library_management_system.model.dto.request.LoginRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.RegisterRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.LoginResponseDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.RegisterResponseDTO;
import com.getir.librarymanagement.library_management_system.model.entity.User;
import com.getir.librarymanagement.library_management_system.model.mapper.UserMapper;
import com.getir.librarymanagement.library_management_system.repository.UserRepository;
import com.getir.librarymanagement.library_management_system.security.jwt.JwtUtil;
import com.getir.librarymanagement.library_management_system.service.impl.AuthServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .password("encodedPassword")
                .phone("1234567890")
                .role(Role.PATRON)
                .registeredDate(LocalDateTime.now())
                .build();
    }

    @Test
    void testRegister_success() {
        RegisterRequestDTO request = new RegisterRequestDTO("Test User", "test@example.com", "password", "1234567890");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.existsByPhone(request.getPhone())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("mock-token");

        RegisterResponseDTO response = authService.register(request);

        assertNotNull(response);
        assertEquals(mockUser.getEmail(), response.getEmail());
        assertEquals("mock-token", response.getToken());
    }

    @Test
    void testRegister_emailAlreadyExists_shouldThrowException() {
        RegisterRequestDTO request = new RegisterRequestDTO("Test User", "test@example.com", "password", "1234567890");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void testRegister_phoneAlreadyExists_shouldThrowException() {
        RegisterRequestDTO request = new RegisterRequestDTO("Test User", "test@example.com", "password", "1234567890");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.existsByPhone(request.getPhone())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void testLogin_success() {
        LoginRequestDTO request = new LoginRequestDTO("test@example.com", "password");

        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("mocked-jwt-token");

        LoginResponseDTO response = authService.login(request);

        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    void testLogin_userNotFound_shouldThrowException() {
        LoginRequestDTO request = new LoginRequestDTO("unknown@example.com", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authService.login(request));
    }

}