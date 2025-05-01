package com.getir.librarymanagement.library_management_system.model.mapper;

import com.getir.librarymanagement.library_management_system.model.dto.response.BorrowResponseDTO;
import com.getir.librarymanagement.library_management_system.model.entity.BorrowRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BorrowRecordMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "book.bookId", target = "bookId")
    @Mapping(source = "borrowRecordId", target = "borrowRecordId")
    BorrowResponseDTO toDto(BorrowRecord entity);
}
