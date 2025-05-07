package com.getir.librarymanagement.library_management_system.repository;

import com.getir.librarymanagement.library_management_system.model.entity.Book;
import com.getir.librarymanagement.library_management_system.model.entity.BorrowRecord;
import com.getir.librarymanagement.library_management_system.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing borrow records.
 * Provides methods to query records by user, book, or return status.
 */
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    /**
     * Finds all borrow records associated with a specific user.
     *
     * @param user the user to search by
     * @return list of borrow records
     */
    List<BorrowRecord> findByUser(User user);

    /**
     * Finds all borrow records associated with a specific book.
     *
     * @param book the book to search by
     * @return list of borrow records
     */
    List<BorrowRecord> findByBook(Book book);

    /**
     * Finds all borrow records that have not been returned yet.
     *
     * @return list of currently borrowed (not returned) records
     */
    List<BorrowRecord> findByIsReturnedFalse();

    /**
     * Checks whether the user has any overdue books (not returned and past due date).
     */
    boolean existsByUserAndIsReturnedFalseAndDueDateBefore(User user, LocalDate date);

    /**
     * Checks whether the user has already borrowed a specific book and has not returned it yet.
     */
    boolean existsByUserAndBookAndIsReturnedFalse(User user, Book book);

    /**
     * Counts the number of currently borrowed (not returned) books for a user.
     *
     * @param user the user whose borrow count is being checked
     * @return the count of active borrow records
     */
    long countByUserAndIsReturnedFalse(User user);
}
