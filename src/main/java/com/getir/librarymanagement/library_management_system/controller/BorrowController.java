package com.getir.librarymanagement.library_management_system.controller;


import com.getir.librarymanagement.library_management_system.model.dto.request.BorrowRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BorrowResponseDTO;
import com.getir.librarymanagement.library_management_system.service.IBorrowService;
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

    @PostMapping
    public ResponseEntity<BorrowResponseDTO> borrowBook(@Valid @RequestBody BorrowRequestDTO borrowRequestDTO) {
        log.info("Borrowing book with id: {}", borrowRequestDTO.getBookId());
        BorrowResponseDTO borrowedBook = borrowService.borrowBook(borrowRequestDTO);
        return new ResponseEntity<>(borrowedBook, HttpStatus.CREATED);
    }

    @PutMapping("/return/{borrowRecordId}")
    public ResponseEntity<BorrowResponseDTO> returnBook(@PathVariable Long borrowRecordId) {
        BorrowResponseDTO returnedBook = borrowService.returnBook(borrowRecordId);
        return ResponseEntity.ok(returnedBook);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowResponseDTO>> getBorrowHistoryByUser(@PathVariable Long userId) {
        List<BorrowResponseDTO> borrowHistory = borrowService.getBorrowHistoryByUser(userId);
        return ResponseEntity.ok(borrowHistory);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowResponseDTO>> getOverdueBooks() {
        List<BorrowResponseDTO> overdueBooks = borrowService.getOverdueBooks();
        return ResponseEntity.ok(overdueBooks);
    }

    @PreAuthorize( "hasRole('LIBRARIAN')")
    @GetMapping
    public ResponseEntity<List<BorrowResponseDTO>> getAllBorrowRecords() {
        List<BorrowResponseDTO> allBorrowRecords = borrowService.getAllBorrowRecords();
        return ResponseEntity.ok(allBorrowRecords);
    }

    @DeleteMapping("/{borrowRecordId}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> deleteBorrowRecord(@PathVariable Long borrowRecordId) {
        borrowService.deleteBorrowRecord(borrowRecordId);
        return ResponseEntity.ok("Borrow Record deleted successfully.");
    }
}
