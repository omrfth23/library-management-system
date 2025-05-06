package com.getir.librarymanagement.library_management_system.model.dto.request;

import lombok.Data;

@Data
public class BookSearchRequestDTO {

    private String title;

    private String author;

    private String isbn;

    private String genre;
}
