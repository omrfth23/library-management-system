package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.request.BorrowRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BorrowResponseDTO;

import java.util.List;

/**
 * Service interface for managing book borrowing operations,
 * including borrowing, returning, listing history, and handling overdue books.
 */
public interface IBorrowService {

    /**
     * Borrows a book for the currently authenticated user.
     *
     * @param borrowRequestDTO request data including book ID and borrow/due dates
     * @return borrowing record as response DTO
     */
    BorrowResponseDTO borrowBook(BorrowRequestDTO borrowRequestDTO);

    /**
     * Marks a borrowed book as returned by its borrow record ID.
     *
     * @param borrowRecordId the ID of the borrow record
     * @return updated borrow record DTO
     */
    BorrowResponseDTO returnBook(Long borrowRecordId);

    /**
     * Retrieves all borrow records for a specific user.
     *
     * @param userId the user ID
     * @return list of borrow records for the user
     */
    List<BorrowResponseDTO> getBorrowHistoryByUser(Long userId);

    /**
     * Retrieves all borrow records in the system.
     *
     * @return list of all borrow records
     */
    List<BorrowResponseDTO> getAllBorrowRecords();

    /**
     * Retrieves all borrow records that are overdue and not yet returned.
     *
     * @return list of overdue borrow records
     */
    List<BorrowResponseDTO> getOverdueBooks();

    /**
     * Generates a report for overdue borrow records.
     *
     * @return formatted report as a string
     */
    String generateOverdueReport();

    /**
     * Deletes a borrow record by its ID.
     *
     * @param borrowRecordId ID of the record to delete
     */
    void deleteBorrowRecord(Long borrowRecordId);
}