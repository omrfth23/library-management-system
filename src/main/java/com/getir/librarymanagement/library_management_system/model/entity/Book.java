package com.getir.librarymanagement.library_management_system.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "books")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id")
    private Long bookId;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    private String author;

    @NotBlank(message = "ISBN cannot be blank")
    @Column(unique = true)
    private String isbn;

    @NotNull(message = "Publication date cannot be null")
    private LocalDate publicationDate;

    @NotBlank(message = "Genre cannot be blank")
    private String genre;

    @NotNull(message = "Quantity cannot be null")
    @Min(0)
    private Integer quantity;

    private boolean available;

}
