package com.getir.librarymanagement.library_management_system.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookAvailabilityEvent {

    private Long bookId;
    private boolean available;
}
