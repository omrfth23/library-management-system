package com.getir.librarymanagement.library_management_system.controller;

import com.getir.librarymanagement.library_management_system.model.dto.request.UserRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.UserSelfUpdateRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.UserResponseDTO;
import com.getir.librarymanagement.library_management_system.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;

    /**
     * Creates a new user. Only accessible by librarians.
     */
    @Operation(summary = "Create user", description = "Creates a new user. Accessible by LIBRARIAN only.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Creating new user with email: {}", userRequestDTO.getEmail());
        UserResponseDTO createdUser = userService.createUser(userRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Retrieves a user by ID. Only accessible by librarians.
     */
    @Operation(summary = "Get user by ID", description = "Fetches a user's information by their ID. Accessible by LIBRARIAN only.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        log.debug("Fetching user with ID: {}", id);
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Returns all users. Only accessible by librarians.
     */
    @Operation(summary = "Get all users", description = "Returns all registered users. Accessible by LIBRARIAN only.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.debug("Fetching all users");
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Updates user information by ID. Only accessible by librarians.
     */
    @Operation(summary = "Update user", description = "Updates a user's information. Accessible by LIBRARIAN only.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                      @Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Updating user with ID: {}", id);
        UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Allows the currently authenticated user to update their own information.
     */
    @Operation(summary = "Update my profile", description = "Allows the current user to update their profile information.")
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyInfo(@RequestBody @Valid UserSelfUpdateRequestDTO dto) {
        log.info("Updating current user's own profile");
        return ResponseEntity.ok(userService.updateMyInfo(dto));
    }

    /**
     * Retrieves the current authenticated user's profile information.
     */
    @Operation(summary = "Get my profile", description = "Returns the current user's profile data.")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile() {
        log.debug("Fetching current user's profile");
        return ResponseEntity.ok(userService.getCurrentUserInfo());
    }

    /**
     * Deletes a user by ID. Only accessible by librarians.
     */
    @Operation(summary = "Delete user", description = "Deletes a user by ID. Accessible by LIBRARIAN only.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        log.warn("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok("User Deleted Successfully");
    }
}
