package com.getir.librarymanagement.library_management_system.controller;


import com.getir.librarymanagement.library_management_system.model.dto.request.BorrowRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BorrowResponseDTO;
import com.getir.librarymanagement.library_management_system.service.IBorrowService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
@Slf4j
public class BorrowController {

    private final IBorrowService borrowService;

    /**
     * Allows a user to borrow a book.
     */
    @Operation(summary = "Borrow a book", description = "Allows a user to borrow a book using their account.")
    @PostMapping
    public ResponseEntity<BorrowResponseDTO> borrowBook(@Valid @RequestBody BorrowRequestDTO borrowRequestDTO) {
        log.info("User is attempting to borrow book with ID: {}", borrowRequestDTO.getBookId());
        BorrowResponseDTO borrowedBook = borrowService.borrowBook(borrowRequestDTO);
        return new ResponseEntity<>(borrowedBook, HttpStatus.CREATED);
    }

    /**
     * Allows a user to return a previously borrowed book.
     */
    @Operation(summary = "Return a borrowed book", description = "Marks a borrowed book as returned by its borrow record ID.")
    @PutMapping("/return/{borrowRecordId}")
    public ResponseEntity<BorrowResponseDTO> returnBook(@PathVariable Long borrowRecordId) {
        log.info("Returning book for borrow record ID: {}", borrowRecordId);
        BorrowResponseDTO returnedBook = borrowService.returnBook(borrowRecordId);
        return ResponseEntity.ok(returnedBook);
    }

    /**
     * Retrieves a specific user's borrowing history. Only accessible to librarians.
     */
    @Operation(summary = "Get borrow history by user", description = "Retrieves borrow history by borrow record ID. Accessible by LIBRARIAN only.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/user/{borrowRecordId}")
    public ResponseEntity<List<BorrowResponseDTO>> getBorrowHistoryByUser(@PathVariable Long borrowRecordId) {
        log.debug("Fetching borrow history for record ID: {}", borrowRecordId);
        List<BorrowResponseDTO> borrowHistory = borrowService.getBorrowHistoryByUser(borrowRecordId);
        return ResponseEntity.ok(borrowHistory);
    }

    /**
     * Lists all overdue borrow records. Only accessible to librarians.
     */
    @Operation(summary = "Get overdue books", description = "Returns a list of all overdue borrowed books. Accessible by LIBRARIAN only.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowResponseDTO>> getOverdueBooks() {
        log.warn("Fetching all overdue borrow records");
        List<BorrowResponseDTO> overdueBooks = borrowService.getOverdueBooks();
        return ResponseEntity.ok(overdueBooks);
    }

    /**
     * Retrieves all borrow records. Only accessible to librarians.
     */
    @Operation(summary = "Get all borrow records", description = "Fetches all borrow records in the system. Accessible by LIBRARIAN only.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping
    public ResponseEntity<List<BorrowResponseDTO>> getAllBorrowRecords() {
        log.debug("Fetching all borrow records");
        List<BorrowResponseDTO> allBorrowRecords = borrowService.getAllBorrowRecords();
        return ResponseEntity.ok(allBorrowRecords);
    }

    /**
     * Deletes a specific borrow record. Only accessible to librarians.
     */
    @Operation(summary = "Delete borrow record", description = "Deletes a borrow record by its ID. Accessible by LIBRARIAN only.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{borrowRecordId}")
    public ResponseEntity<String> deleteBorrowRecord(@PathVariable Long borrowRecordId) {
        log.warn("Deleting borrow record with ID: {}", borrowRecordId);
        borrowService.deleteBorrowRecord(borrowRecordId);
        return ResponseEntity.ok("Borrow Record deleted successfully.");
    }
}