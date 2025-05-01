package com.getir.librarymanagement.library_management_system.model.mapper;

import com.getir.librarymanagement.library_management_system.model.dto.request.BookRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BookResponseDTO;
import com.getir.librarymanagement.library_management_system.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookMapper {


    Book toEntity(BookRequestDTO dto);

    @Mapping(source = "bookId", target = "bookId")
    BookResponseDTO toDto(Book entity);

    void updateBookFromDto(BookRequestDTO dto, @MappingTarget Book entity);
}
