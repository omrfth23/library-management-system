package com.getir.librarymanagement.library_management_system.repository;

import com.getir.librarymanagement.library_management_system.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repository interface for Book entity.
 * Provides CRUD operations and support for dynamic filtering via specifications.
 */
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    /**
     * Finds a book by its ISBN.
     *
     * @param isbn unique identifier of the book
     * @return optional containing the book if found
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Checks whether a book with the given ISBN already exists.
     *
     * @param isbn unique identifier to check
     * @return true if a book with the ISBN exists
     */
    boolean existsByIsbn(String isbn);
}
