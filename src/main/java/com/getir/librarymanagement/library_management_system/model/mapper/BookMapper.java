package com.getir.librarymanagement.library_management_system.model.mapper;

import com.getir.librarymanagement.library_management_system.model.dto.request.BookRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BookResponseDTO;
import com.getir.librarymanagement.library_management_system.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for converting between Book entities and DTOs.
 * Used by the service layer to separate domain and transport models.
 */
@Mapper(componentModel = "spring")
public interface BookMapper {

    /**
     * Converts a BookRequestDTO to a Book entity.
     *
     * @param dto the request data
     * @return mapped Book entity
     */
    Book toEntity(BookRequestDTO dto);

    /**
     * Converts a Book entity to BookResponseDTO.
     *
     * @param entity the Book entity
     * @return response DTO with book data
     */
    @Mapping(source = "bookId", target = "bookId") // Optional: explicit mapping for clarity
    BookResponseDTO toDto(Book entity);

    /**
     * Updates an existing Book entity with fields from BookRequestDTO.
     * Fields not present in the DTO are left unchanged in the entity.
     *
     * @param dto    new data
     * @param entity the entity to update
     */
    void updateBookFromDto(BookRequestDTO dto, @MappingTarget Book entity);
}
