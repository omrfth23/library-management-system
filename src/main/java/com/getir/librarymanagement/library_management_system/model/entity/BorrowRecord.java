package com.getir.librarymanagement.library_management_system.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a borrowing record in the system.
 * Links a user with a book for a given time period.
 */
@Entity
@Table(name = "borrow_records")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "borrow_id")
    private Long borrowRecordId;

    @NotNull(message = "Borrow date cannot be null")
    private LocalDate borrowDate;

    @NotNull(message = "Due date cannot be null")
    private LocalDate dueDate;

    private LocalDate returnDate;

    /**
     * Indicates whether the book has been returned.
     * Defaults to false when a record is created.
     */
    private Boolean isReturned = false;

    /**
     * Many-to-one relationship to the user who borrowed the book.
     * Eager fetch ensures the user is loaded with the borrow record.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // Prevents infinite recursion in JSON serialization
    private User user;

    /**
     * Many-to-one relationship to the book being borrowed.
     * Eager fetch ensures the book is available with the borrow record.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
