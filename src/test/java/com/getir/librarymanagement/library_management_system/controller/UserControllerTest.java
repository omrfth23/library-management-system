package com.getir.librarymanagement.library_management_system.controller;

import com.getir.librarymanagement.library_management_system.enums.Role;
import com.getir.librarymanagement.library_management_system.model.dto.request.UserRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.UserSelfUpdateRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.UserResponseDTO;
import com.getir.librarymanagement.library_management_system.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponseDTO getMockUser() {
        return UserResponseDTO.builder()
                .userId(1L)
                .name("Test User")
                .email("test@example.com")
                .phone("5551234567")
                .role(Role.PATRON)
                .registeredDate(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateUser_success() {
        UserRequestDTO request = UserRequestDTO.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .phone("5551234567")
                .role(Role.PATRON)
                .build();

        when(userService.createUser(request)).thenReturn(getMockUser());

        ResponseEntity<UserResponseDTO> response = userController.createUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().getEmail());
    }

    @Test
    void testGetUserById_success() {
        when(userService.getUserById(1L)).thenReturn(getMockUser());

        ResponseEntity<UserResponseDTO> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getUserId());
    }

    @Test
    void testGetAllUsers_success() {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(getMockUser()));

        ResponseEntity<List<UserResponseDTO>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testUpdateUser_success() {
        UserRequestDTO updateRequest = UserRequestDTO.builder()
                .name("Updated User")
                .email("test@example.com")
                .password("newpass")
                .phone("5557654321")
                .role(Role.PATRON)
                .build();

        UserResponseDTO updatedUser = getMockUser();
        updatedUser.setName("Updated User");
        updatedUser.setPhone("5557654321");

        when(userService.updateUser(1L, updateRequest)).thenReturn(updatedUser);

        ResponseEntity<UserResponseDTO> response = userController.updateUser(1L, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated User", response.getBody().getName());
    }

    @Test
    void testUpdateMyInfo_success() {
        UserSelfUpdateRequestDTO request = UserSelfUpdateRequestDTO.builder()
                .name("Self Updated")
                .phone("5559876543")
                .build();

        UserResponseDTO updatedUser = getMockUser();
        updatedUser.setName("Self Updated");
        updatedUser.setPhone("5559876543");

        when(userService.updateMyInfo(request)).thenReturn(updatedUser);

        ResponseEntity<UserResponseDTO> response = userController.updateMyInfo(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Self Updated", response.getBody().getName());
    }

    @Test
    void testGetMyProfile_success() {
        when(userService.getCurrentUserInfo()).thenReturn(getMockUser());

        ResponseEntity<UserResponseDTO> response = userController.getMyProfile();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test@example.com", response.getBody().getEmail());
    }

    @Test
    void testDeleteUser_success() {
        when(userService.deleteUser(1L)).thenReturn("User Deleted Succesfully!");

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(1L);
    }
}

