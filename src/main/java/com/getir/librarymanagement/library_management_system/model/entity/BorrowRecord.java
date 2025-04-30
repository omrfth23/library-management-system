package com.getir.librarymanagement.library_management_system.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "borrow_records")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Borrow date cannot be null")
    private LocalDate borrowDate;

    @NotNull(message = "Due date cannot be null")
    private LocalDate dueDate;

    private LocalDate returnDate; // iade edilmediyse null olur

    private Boolean isReturned = false; // default = false

    //User obejsi ile Many-One ilişki
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    //Book objesi Many-One şeklinde ilişki
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
