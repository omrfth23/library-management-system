package com.getir.librarymanagement.library_management_system.model.mapper;

import com.getir.librarymanagement.library_management_system.model.dto.request.RegisterRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.UserRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BorrowResponseDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.UserResponseDTO;
import com.getir.librarymanagement.library_management_system.model.entity.BorrowRecord;
import com.getir.librarymanagement.library_management_system.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper interface for converting between User-related DTOs and entity classes.
 * Helps isolate transformation logic from business logic.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts UserRequestDTO to User entity.
     * ID and registeredDate are intentionally ignored (they're set internally).
     *
     * @param dto user creation/update request
     * @return new User entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredDate", ignore = true)
    User toEntity(UserRequestDTO dto);

    /**
     * Converts RegisterRequestDTO to User entity.
     * ID and registration date are set manually in the service layer.
     *
     * @param dto user registration request
     * @return new User entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredDate", ignore = true)
    User toEntity(RegisterRequestDTO dto);

    /**
     * Converts a User entity to UserResponseDTO.
     * Maps the 'id' field to 'userId' in the DTO.
     *
     * @param entity User entity
     * @return DTO for returning user data to client
     */
    @Mapping(source = "id", target = "userId")
    UserResponseDTO toResponseDTO(User entity);

    /**
     * Converts a list of BorrowRecord entities to a list of BorrowResponseDTOs.
     *
     * @param entities list of borrow records
     * @return mapped list of DTOs
     */
    @Mapping(target = "borrowRecords", source = "borrowRecords")
    List<BorrowResponseDTO> toBorrowResponseDTOList(List<BorrowRecord> entities);

    /**
     * Updates a User entity from UserRequestDTO fields.
     * Used during PUT operations.
     *
     * @param dto    the request data
     * @param entity the user entity to update
     */
    void updateUserFromDto(UserRequestDTO dto, @MappingTarget User entity);
}
