package com.getir.librarymanagement.library_management_system.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BorrowResponseDTO {

    private Long borrowRecordId;
    private Long userId;
    private String userName;
    private Long bookId;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Boolean isReturned;

}
