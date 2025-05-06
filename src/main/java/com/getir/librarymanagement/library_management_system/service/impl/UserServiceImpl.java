package com.getir.librarymanagement.library_management_system.service.impl;

import com.getir.librarymanagement.library_management_system.enums.Role;
import com.getir.librarymanagement.library_management_system.model.dto.request.UserRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.UserSelfUpdateRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.UserResponseDTO;
import com.getir.librarymanagement.library_management_system.model.entity.User;
import com.getir.librarymanagement.library_management_system.model.mapper.UserMapper;
import com.getir.librarymanagement.library_management_system.repository.UserRepository;
import com.getir.librarymanagement.library_management_system.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Returns the currently authenticated user's email.
     */
    private String getAuthenticatedEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    /**
     * Creates a new user account.
     */
    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        User user = userMapper.toEntity(userRequestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegisteredDate(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        log.info("New user created with ID: {}", savedUser.getId());
        return userMapper.toResponseDTO(savedUser);
    }

    /**
     * Retrieves a user by ID.
     */
    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("User not found with id: " + id);
                });

        return userMapper.toResponseDTO(user);
    }

    /**
     * Retrieves all registered users.
     */
    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.debug("Fetched all users. Total count: {}", users.size());
        return users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns the currently authenticated user's info.
     */
    @Override
    public UserResponseDTO getCurrentUserInfo() {
        String email = getAuthenticatedEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("User Not Found.");
                });

        return userMapper.toResponseDTO(user);
    }

    /**
     * Updates a user's profile by their ID.
     */
    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("User not found with id: " + id);
                });

        userMapper.updateUserFromDto(userRequestDTO, user);
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully. ID: {}", id);
        return userMapper.toResponseDTO(updatedUser);
    }

    /**
     * Allows the current user to update their own profile details.
     */
    @Override
    public UserResponseDTO updateMyInfo(UserSelfUpdateRequestDTO dto) {
        String currentEmail = getAuthenticatedEmail();

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("User not found.");
                });

        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        if (!user.getPhone().equals(dto.getPhone()) && userRepository.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("Phone number already in use.");
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User updated = userRepository.save(user);
        log.info("User updated their own profile. Email: {}", updated.getEmail());
        return userMapper.toResponseDTO(updated);
    }

    /**
     * Deletes a user by ID.
     */
    @Override
    public String deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        log.warn("User deleted. ID: {}", id);
        return "User Deleted Successfully!";
    }
}
