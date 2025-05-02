package com.getir.librarymanagement.library_management_system.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class BookResponseDTO {
    private Long bookId;
    private String title;
    private String author;
    private String isbn;
    private LocalDate publicationDate;
    private String genre;
    private Integer quantity;
    private boolean available;

}
