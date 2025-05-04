package com.getir.librarymanagement.library_management_system.model.dto.request;

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
public class BorrowRequestDTO {

    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    @NotNull(message = "Borrow date cannot be null")
    private LocalDate borrowDate;

    @NotNull(message = "Due date cannot be null")
    private LocalDate dueDate;
}
