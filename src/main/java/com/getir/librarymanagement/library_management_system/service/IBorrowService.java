package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.request.BorrowRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BorrowResponseDTO;

import java.util.List;

public interface IBorrowService {

    BorrowResponseDTO borrowBook(BorrowRequestDTO borrowRequestDTO);

    BorrowResponseDTO returnBook(Long borrowRecordId);

    List<BorrowResponseDTO> getBorrowHistoryByUser(Long userId);

    List<BorrowResponseDTO> getAllBorrowRecords();

    List<BorrowResponseDTO> getOverdueBooks();

    void deleteBorrowRecord(Long borrowRecordId);
}
