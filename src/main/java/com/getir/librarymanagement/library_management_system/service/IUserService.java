package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.request.UserRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.UserSelfUpdateRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.UserResponseDTO;

import java.util.List;

/**
 * Service interface for handling user-related operations such as
 * creation, retrieval, update, self-update, and deletion.
 */
public interface IUserService {

    /**
     * Creates a new user in the system.
     * Only librarians are authorized to perform this action.
     *
     * @param userRequestDTO user creation data
     * @return created user's response DTO
     */
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    /**
     * Retrieves a specific user by ID.
     * Accessible only by librarians.
     *
     * @param id user ID
     * @return corresponding user's response DTO
     */
    UserResponseDTO getUserById(Long id);

    /**
     * Retrieves all users in the system.
     * Accessible only by librarians.
     *
     * @return list of user response DTOs
     */
    List<UserResponseDTO> getAllUsers();

    /**
     * Retrieves information of the currently authenticated user.
     *
     * @return current user's response DTO
     */
    UserResponseDTO getCurrentUserInfo();

    /**
     * Updates a user's information by ID.
     * Only librarians can update other users.
     *
     * @param id              user ID
     * @param userRequestDTO  updated user data
     * @return updated user's response DTO
     */
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    /**
     * Allows the currently authenticated user to update their own profile.
     *
     * @param dto self-update request data
     * @return updated profile DTO
     */
    UserResponseDTO updateMyInfo(UserSelfUpdateRequestDTO dto);

    /**
     * Deletes a user by their ID.
     * Only librarians can perform this action.
     *
     * @param id user ID
     * @return success message
     */
    String deleteUser(Long id);
}
