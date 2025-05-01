package com.getir.librarymanagement.library_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.getir.librarymanagement.library_management_system.model.entity.Book;
import com.getir.librarymanagement.library_management_system.model.entity.BorrowRecord;
import com.getir.librarymanagement.library_management_system.model.entity.User;


import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByUser(User user);

    List<BorrowRecord> findByBook(Book book);

    List<BorrowRecord> findByIsReturnedFalse(); // İade edilmemiş kayıtlar için.
}
