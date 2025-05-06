package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.request.BookRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.BookSearchRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BookResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for managing book-related operations such as
 * creation, retrieval, update, deletion, and search functionality.
 */
public interface IBookService {

    /**
     * Adds a new book to the system.
     *
     * @param bookRequestDTO DTO containing book details
     * @return the created book's response data
     */
    BookResponseDTO addBook(BookRequestDTO bookRequestDTO);

    /**
     * Retrieves a book by its ID.
     *
     * @param bookId the unique ID of the book
     * @return the book's response data
     */
    BookResponseDTO getBookById(Long bookId);

    /**
     * Retrieves all books from the system.
     *
     * @return list of all books
     */
    List<BookResponseDTO> getAllBooks();

    /**
     * Searches for books using filters and pagination.
     *
     * @param filters   optional filter criteria (title, author, isbn, genre)
     * @param pageable  pagination parameters (page number, size)
     * @return paged result of books matching the criteria
     */
    Page<BookResponseDTO> searchBooks(BookSearchRequestDTO filters, Pageable pageable);

    /**
     * Updates an existing book with the given ID using provided data.
     *
     * @param bookId         ID of the book to update
     * @param bookRequestDTO DTO with updated book details
     * @return updated book response
     */
    BookResponseDTO updateBook(Long bookId, BookRequestDTO bookRequestDTO);

    /**
     * Deletes a book by its ID.
     *
     * @param bookId the ID of the book to delete
     * @return success message
     */
    String deleteBook(Long bookId);
}
