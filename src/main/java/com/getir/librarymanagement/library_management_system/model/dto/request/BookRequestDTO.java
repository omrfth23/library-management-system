package com.getir.librarymanagement.library_management_system.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequestDTO {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    private String author;

    @NotBlank(message = "ISBN cannot be blank")
    private String isbn;

    @NotNull(message = "Publication date cannot be null")
    private LocalDate publicationDate;

    @NotBlank(message = "Genre cannot be blank")
    private String genre;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

}
