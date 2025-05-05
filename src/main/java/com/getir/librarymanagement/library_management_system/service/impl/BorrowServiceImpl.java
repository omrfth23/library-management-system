package com.getir.librarymanagement.library_management_system.service.impl;

import com.getir.librarymanagement.library_management_system.exception.BookOutOfStockException;
import com.getir.librarymanagement.library_management_system.model.dto.request.BorrowRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BorrowResponseDTO;
import com.getir.librarymanagement.library_management_system.model.entity.Book;
import com.getir.librarymanagement.library_management_system.model.entity.BorrowRecord;
import com.getir.librarymanagement.library_management_system.model.entity.User;
import com.getir.librarymanagement.library_management_system.model.mapper.BorrowRecordMapper;
import com.getir.librarymanagement.library_management_system.repository.BookRepository;
import com.getir.librarymanagement.library_management_system.repository.BorrowRecordRepository;
import com.getir.librarymanagement.library_management_system.repository.UserRepository;
import com.getir.librarymanagement.library_management_system.service.IBorrowService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements IBorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordMapper borrowRecordMapper;

    @Override
    public BorrowResponseDTO borrowBook(BorrowRequestDTO borrowRequestDTO) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        Book book = bookRepository.findById(borrowRequestDTO.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (book.getQuantity() <= 0) {
            throw new BookOutOfStockException("Book is not available for borrowing.");
        }

        BorrowRecord borrowRecord = BorrowRecord.builder()
                .user(user)
                .book(book)
                .borrowDate(borrowRequestDTO.getBorrowDate())
                .dueDate(borrowRequestDTO.getDueDate())
                .isReturned(false)
                .build();


        book.setQuantity(book.getQuantity() - 1);
        book.setAvailable(book.getQuantity() > 0);
        bookRepository.save(book);

        BorrowRecord savedRecord = borrowRecordRepository.save(borrowRecord);
        return borrowRecordMapper.toDto(savedRecord);
    }

    @Override
    public BorrowResponseDTO returnBook(Long borrowRecordId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new EntityNotFoundException("Borrow record not found"));

        if (borrowRecord.getIsReturned()) {
            throw new IllegalStateException("Book already returned");
        }

        borrowRecord.setReturnDate(LocalDate.now());
        borrowRecord.setIsReturned(true);

        Book book = borrowRecord.getBook();
        book.setQuantity(book.getQuantity() + 1);
        book.setAvailable(true);
        bookRepository.save(book);



        BorrowRecord updatedRecord = borrowRecordRepository.save(borrowRecord);
        return borrowRecordMapper.toDto(updatedRecord);
    }

    @Override
    public List<BorrowResponseDTO> getBorrowHistoryByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<BorrowRecord> borrowRecords = borrowRecordRepository.findByUser(user);

        return borrowRecords.stream()
                .map(borrowRecordMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowResponseDTO> getAllBorrowRecords() {
        return borrowRecordRepository.findAll()
                .stream()
                .map(borrowRecordMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowResponseDTO> getOverdueBooks() {
        List<BorrowRecord> overdueRecords = borrowRecordRepository.findByIsReturnedFalse()
                .stream()
                .filter(record -> record.getDueDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());

        return overdueRecords.stream()
                .map(borrowRecordMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBorrowRecord(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new EntityNotFoundException("Borrow Record Not Found."));

        borrowRecordRepository.delete(record);
    }
}
