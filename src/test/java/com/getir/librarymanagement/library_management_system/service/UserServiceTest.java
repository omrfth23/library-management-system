package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.request.UserRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.UserSelfUpdateRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.UserResponseDTO;
import com.getir.librarymanagement.library_management_system.repository.UserRepository;
import com.getir.librarymanagement.library_management_system.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.getir.librarymanagement.library_management_system.enums.Role;

@SpringBootTest
@ActiveProfiles("test") // application-test.properties
@AutoConfigureTestDatabase
class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testCreateUser_success() {
        // given
        UserRequestDTO request = UserRequestDTO.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .phone("1234567890")
                .role(Role.PATRON)
                .build();

        // when
        UserResponseDTO response = userService.createUser(request);

        // then
        assertNotNull(response);
        assertNotNull(response.getUserId());
        assertEquals("John Doe", response.getName());
        assertEquals("john.doe@example.com", response.getEmail());
    }

    @Test
    void testCreateUser_duplicateEmail_shouldThrowException() {
        UserRequestDTO request = UserRequestDTO.builder()
                .name("Duplicate")
                .email("dup@example.com")
                .password("pass")
                .phone("1111111111")
                .role(Role.PATRON)
                .build();

        userService.createUser(request);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(request));

        assertEquals("Email is already in use.", ex.getMessage());
    }

    @Test
    void testGetUserById_success() {
        UserRequestDTO request = UserRequestDTO.builder()
                .name("Jane")
                .email("jane@example.com")
                .password("secret")
                .phone("5551231234")
                .role(Role.LIBRARIAN)
                .build();

        UserResponseDTO createdUser = userService.createUser(request);
        UserResponseDTO foundUser = userService.getUserById(createdUser.getUserId());

        assertEquals(createdUser.getUserId(), foundUser.getUserId());
        assertEquals("Jane", foundUser.getName());
    }

    @Test
    void testUpdateUser_success() {
        UserRequestDTO request = UserRequestDTO.builder()
                .name("Tom")
                .email("tom@example.com")
                .password("oldpass")
                .phone("5551112222")
                .role(Role.LIBRARIAN)
                .build();

        UserResponseDTO createdUser = userService.createUser(request);

        UserRequestDTO updatedRequest = UserRequestDTO.builder()
                .name("Tom Updated")
                .email("tom@example.com")
                .password("oldpass")
                .phone("5553334444")
                .role(Role.LIBRARIAN)
                .build();

        UserResponseDTO updatedUser = userService.updateUser(createdUser.getUserId(), updatedRequest);

        assertEquals("Tom Updated", updatedUser.getName());
        assertEquals("5553334444", updatedUser.getPhone());
    }

    @Test
    void testDeleteUser_success() {
        UserRequestDTO request = UserRequestDTO.builder()
                .name("Delete Me")
                .email("delete@example.com")
                .password("password")
                .phone("0000000000")
                .role(Role.PATRON)
                .build();

        UserResponseDTO createdUser = userService.createUser(request);

        String result = userService.deleteUser(createdUser.getUserId());

        assertEquals("User Deleted Succesfully!", result);
        assertFalse(userRepository.findById(createdUser.getUserId()).isPresent());
    }

    @Test
    void testGetAllUsers_returnsList() {
        UserRequestDTO request1 = UserRequestDTO.builder()
                .name("User One")
                .email("one@example.com")
                .password("123")
                .phone("1111111111")
                .role(Role.PATRON)
                .build();

        UserRequestDTO request2 = UserRequestDTO.builder()
                .name("User Two")
                .email("two@example.com")
                .password("456")
                .phone("2222222222")
                .role(Role.LIBRARIAN)
                .build();

        userService.createUser(request1);
        userService.createUser(request2);

        List<UserResponseDTO> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void testGetCurrentUserInfo_success() {
        // given
        String email = "current@example.com";

        UserRequestDTO request = UserRequestDTO.builder()
                .name("Current User")
                .email(email)
                .password("secure")
                .phone("9999999999")
                .role(Role.PATRON)
                .build();

        userService.createUser(request);

        // simulate authenticated user
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(email, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // when
        UserResponseDTO currentUser = userService.getCurrentUserInfo();

        // then
        assertNotNull(currentUser);
        assertEquals(email, currentUser.getEmail());
        assertEquals("Current User", currentUser.getName());
    }

    @Test
    void testUpdateMyInfo_success() {
        // given
        String email = "myinfo@example.com";

        userService.createUser(UserRequestDTO.builder()
                .name("Original Name")
                .email(email)
                .password("123456")
                .phone("1112223333")
                .role(Role.PATRON)
                .build());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(email, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // when
        UserSelfUpdateRequestDTO updateRequest = UserSelfUpdateRequestDTO.builder()
                .name("Updated Name")
                .email(email)
                .phone("9998887777")
                .password("newpass")
                .build();

        UserResponseDTO updatedUser = userService.updateMyInfo(updateRequest);
        // then
        assertNotNull(updatedUser);
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("9998887777", updatedUser.getPhone());

        assertTrue(passwordEncoder.matches("newpass", userRepository.findByEmail(email).get().getPassword()));
    }

    @Test
    void testUpdateMyInfo_withDuplicatePhone_shouldThrowException() {
        String email = "conflict@example.com";

        userService.createUser(UserRequestDTO.builder()
                .name("User One")
                .email(email)
                .password("123456")
                .phone("1112223333")
                .role(Role.PATRON)
                .build());

        userService.createUser(UserRequestDTO.builder()
                .name("User Two")
                .email("other@example.com")
                .password("123456")
                .phone("9998887777")
                .role(Role.PATRON)
                .build());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(email, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserSelfUpdateRequestDTO updateRequest = UserSelfUpdateRequestDTO.builder()
                .name("Updated")
                .email(email)
                .phone("9998887777") // already taken phone
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.updateMyInfo(updateRequest));

        assertEquals("Phone number already in use.", ex.getMessage());
    }
}
