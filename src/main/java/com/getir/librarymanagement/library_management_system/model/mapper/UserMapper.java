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
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredDate", ignore = true)
    User toEntity(UserRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredDate", ignore = true)
    User toEntity(RegisterRequestDTO dto);

    @Mapping(source = "id", target = "userId")
    UserResponseDTO toResponseDTO(User entity);

    @Mapping(target = "borrowRecords", source = "borrowRecords")
    List<BorrowResponseDTO> toBorrowResponseDTOList(List<BorrowRecord> entities);

   void updateUserFromDto(UserRequestDTO dto, @MappingTarget User entity);


}
