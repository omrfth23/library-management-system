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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowServiceImpl implements IBorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordMapper borrowRecordMapper;

    /**
     * Handles the borrowing process of a book by the current authenticated user.
     */
    @Override
    public BorrowResponseDTO borrowBook(BorrowRequestDTO borrowRequestDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("User not found.");
                });

        long activeBorrowCount = borrowRecordRepository.countByUserAndIsReturnedFalse(user);

        if (activeBorrowCount >= 3) {
            throw new IllegalStateException("You have already borrowed the maximum number of books (3). Please return a book to borrow a new one.");
        }

        boolean hasOverdue = borrowRecordRepository
                .existsByUserAndIsReturnedFalseAndDueDateBefore(user, LocalDate.now());

        if (hasOverdue) {
            throw new IllegalStateException("You have overdue books. Please return them before borrowing another.");
        }

        Book book = bookRepository.findById(borrowRequestDTO.getBookId())
                .orElseThrow(() -> {
                    return new EntityNotFoundException("Book not found");
                });

        boolean alreadyBorrowed = borrowRecordRepository
                .existsByUserAndBookAndIsReturnedFalse(user, book);
        if (alreadyBorrowed) {
            throw new IllegalStateException("You have already borrowed this book and not returned it.");
        }

        if (book.getQuantity() <= 0) {
            throw new BookOutOfStockException("Book is not available for borrowing.");
        }

        LocalDate borrowDate = borrowRequestDTO.getBorrowDate();
        LocalDate today = LocalDate.now();

        if (borrowDate.isAfter(today)) {
            throw new IllegalArgumentException("Borrow date cannot be in the future. Please select today's date or a past date.");
        }

        LocalDate dueDate = borrowDate.plusDays(10);

        BorrowRecord borrowRecord = BorrowRecord.builder()
                .user(user)
                .book(book)
                .borrowDate(borrowRequestDTO.getBorrowDate())
                .dueDate(dueDate)
                .isReturned(false)
                .build();

        book.setQuantity(book.getQuantity() - 1);
        book.setAvailable(book.getQuantity() > 0);
        bookRepository.save(book);

        BorrowRecord savedRecord = borrowRecordRepository.save(borrowRecord);
        log.info("Book borrowed successfully. Book ID: {}, User: {}", book.getBookId(), email);
        return borrowRecordMapper.toDto(savedRecord);
    }

    /**
     * Handles the return process of a borrowed book.
     */
    @Override
    public BorrowResponseDTO returnBook(Long borrowRecordId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("Borrow record not found");
                });

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
        log.info("Book returned successfully. Borrow Record ID: {}", borrowRecordId);
        return borrowRecordMapper.toDto(updatedRecord);
    }

    /**
     * Retrieves the borrowing history for a given user.
     */
    @Override
    public List<BorrowResponseDTO> getBorrowHistoryByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("User not found");
                });

        List<BorrowRecord> borrowRecords = borrowRecordRepository.findByUser(user);
        log.debug("Fetched {} borrow records for user ID: {}", borrowRecords.size(), userId);
        return borrowRecords.stream()
                .map(borrowRecordMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all borrow records in the system.
     */
    @Override
    public List<BorrowResponseDTO> getAllBorrowRecords() {
        List<BorrowRecord> records = borrowRecordRepository.findAll();
        log.debug("Fetched all borrow records. Count: {}", records.size());
        return records.stream()
                .map(borrowRecordMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all overdue (non-returned and past due date) borrow records.
     */
    @Override
    public List<BorrowResponseDTO> getOverdueBooks() {
        List<BorrowRecord> overdueRecords = borrowRecordRepository.findByIsReturnedFalse()
                .stream()
                .filter(record -> record.getDueDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());

        log.debug("Found {} overdue borrow records", overdueRecords.size());
        return overdueRecords.stream()
                .map(borrowRecordMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Generates a report for overdue borrow records.
     */
    public String generateOverdueReport() {
        List<BorrowRecord> borrowRecords = borrowRecordRepository.findAll();
        int totalBorrows = borrowRecords.size();
        int overdueCount = countOverdueBooks(borrowRecords);
        int notReturnedCount = countNotReturnedBooks(borrowRecords);
        int returnedCount = countReturnedBooks(borrowRecords);

        return """
                LIBRARY MANAGEMENT - OVERDUE REPORT
                -----------------------------------
                Total Borrow Records: %d
                Overdue Books: %d
                Not Returned: %d
                Returned Books: %d
                
                Report Generated: %s
                """.formatted(
                totalBorrows,
                overdueCount,
                notReturnedCount,
                returnedCount,
                LocalDateTime.now()
        );
    }

    private int countOverdueBooks(List<BorrowRecord> borrowRecords) {
        return (int) borrowRecords.stream()
                .filter(record -> !record.getIsReturned() && record.getDueDate().isBefore(LocalDate.now()))
                .count();
    }

    private int countNotReturnedBooks(List<BorrowRecord> borrowRecords) {
        return (int) borrowRecords.stream()
                .filter(record -> !record.getIsReturned())
                .count();
    }

    private int countReturnedBooks(List<BorrowRecord> borrowRecords) {
        return (int) borrowRecords.stream()
                .filter(BorrowRecord::getIsReturned)
                .count();
    }

    /**
     * Deletes a borrow record by its ID.
     */
    @Override
    public void deleteBorrowRecord(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("Borrow Record Not Found.");
                });

        borrowRecordRepository.delete(record);
        log.warn("Borrow record deleted. ID: {}", borrowRecordId);
    }
}