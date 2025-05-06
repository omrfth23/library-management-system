package com.getir.librarymanagement.library_management_system.service.impl;

import com.getir.librarymanagement.library_management_system.model.dto.request.BookRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.BookSearchRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BookResponseDTO;
import com.getir.librarymanagement.library_management_system.model.entity.Book;
import com.getir.librarymanagement.library_management_system.model.mapper.BookMapper;
import com.getir.librarymanagement.library_management_system.repository.BookRepository;
import com.getir.librarymanagement.library_management_system.service.IBookService;
import com.getir.librarymanagement.library_management_system.util.BookSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements IBookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    /**
     * Adds a new book to the system.
     * Throws an error if ISBN already exists.
     */
    @Override
    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO) {
        if (bookRepository.existsByIsbn(bookRequestDTO.getIsbn())) {
            throw new IllegalArgumentException("ISBN is already in use.");
        }

        Book book = bookMapper.toEntity(bookRequestDTO);
        book.setAvailable(book.getQuantity() > 0);
        Book savedBook = bookRepository.save(book);
        log.info("Book added successfully with ID: {}", savedBook.getBookId());
        return bookMapper.toDto(savedBook);
    }

    /**
     * Fetches a book by its ID. Throws exception if not found.
     */
    @Override
    public BookResponseDTO getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("Book not found with id: " + bookId);
                });
        return bookMapper.toDto(book);
    }

    /**
     * Retrieves all books from the system.
     */
    @Override
    public List<BookResponseDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        log.debug("Fetched all books, count: {}", books.size());
        return books.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Searches books using dynamic filters and pagination.
     */
    @Override
    public Page<BookResponseDTO> searchBooks(BookSearchRequestDTO filters, Pageable pageable) {
        Page<Book> books = bookRepository.findAll(BookSpecification.withFilters(filters), pageable);
        log.debug("Book search executed with filters: {}", filters);
        return books.map(bookMapper::toDto);
    }

    /**
     * Updates a book by its ID. Merges request data into the entity.
     */
    @Override
    public BookResponseDTO updateBook(Long bookId, BookRequestDTO bookRequestDTO) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("Book not found with id: " + bookId);
                });

        bookMapper.updateBookFromDto(bookRequestDTO, book);
        Book updatedBook = bookRepository.save(book);
        log.info("Book updated successfully. ID: {}", updatedBook.getBookId());
        return bookMapper.toDto(updatedBook);
    }

    /**
     * Deletes a book by ID if it exists.
     */
    @Override
    public String deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Book not found with id: " + bookId);
        }
        bookRepository.deleteById(bookId);
        log.warn("Book deleted. ID: {}", bookId);
        return "Book Deleted Successfully!";
    }
}
