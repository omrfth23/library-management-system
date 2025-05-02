package com.getir.librarymanagement.library_management_system.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BorrowRequestDTO {

    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    @NotNull(message = "Borrow date cannot be null")
    private LocalDate borrowDate;

    @NotNull(message = "Due date cannot be null")
    private LocalDate dueDate;
}
