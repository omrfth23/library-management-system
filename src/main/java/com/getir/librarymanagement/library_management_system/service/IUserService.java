package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.request.UserRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.UserSelfUpdateRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.UserResponseDTO;
import com.getir.librarymanagement.library_management_system.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getCurrentUserInfo();

    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    UserResponseDTO updateMyInfo(UserSelfUpdateRequestDTO dto);

    String deleteUser(Long id);

}
