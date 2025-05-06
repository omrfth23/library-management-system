package com.getir.librarymanagement.library_management_system.model.mapper;

import com.getir.librarymanagement.library_management_system.model.dto.response.BorrowResponseDTO;
import com.getir.librarymanagement.library_management_system.model.entity.BorrowRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting BorrowRecord entities into BorrowResponseDTO.
 * Maps nested user and book fields into flat DTO fields for response representation.
 */
@Mapper(componentModel = "spring")
public interface BorrowRecordMapper {

    /**
     * Converts a BorrowRecord entity to a BorrowResponseDTO.
     * Maps nested entity fields (user and book) into flat DTO structure.
     *
     * @param entity the BorrowRecord entity
     * @return DTO with flattened and readable fields
     */
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "book.bookId", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "borrowRecordId", target = "borrowRecordId")
    BorrowResponseDTO toDto(BorrowRecord entity);
}
