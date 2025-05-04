package com.getir.librarymanagement.library_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400 döner
public class BookOutOfStockException extends RuntimeException {
    public BookOutOfStockException(String message) {
        super(message);
    }
}