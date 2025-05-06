package com.getir.librarymanagement.library_management_system.controller;

import com.getir.librarymanagement.library_management_system.model.dto.request.BookRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.BookSearchRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BookResponseDTO;
import com.getir.librarymanagement.library_management_system.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final IBookService bookService;

    /**
     * Adds a new book to the system. Only librarians are authorized.
     */
    @Operation(summary = "Add new book", description = "Adds a new book to the library. Accessible by LIBRARIAN only.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping
    public ResponseEntity<BookResponseDTO> addBook(@Valid @RequestBody BookRequestDTO bookRequestDTO) {
        log.info("Adding new book: {}", bookRequestDTO.getTitle());
        BookResponseDTO createdBook = bookService.addBook(bookRequestDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    /**
     * Retrieves book details by its ID.
     */
    @Operation(summary = "Get book by ID", description = "Fetches details of a book using its ID.")
    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long bookId) {
        log.debug("Fetching book with ID: {}", bookId);
        BookResponseDTO book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }

    /**
     * Retrieves all books in the library.
     */
    @Operation(summary = "Get all books", description = "Returns a list of all books in the library.")
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        log.debug("Fetching all books");
        List<BookResponseDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * Searches books based on optional filter parameters with pagination support.
     */
    @Operation(summary = "Search books", description = "Searches books by title, author, ISBN, or genre. Supports pagination.")
    @GetMapping("/search")
    public ResponseEntity<Page<BookResponseDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("Searching books with filters - title: {}, author: {}, isbn: {}, genre: {}", title, author, isbn, genre);
        BookSearchRequestDTO filters = new BookSearchRequestDTO();
        filters.setTitle(title);
        filters.setAuthor(author);
        filters.setIsbn(isbn);
        filters.setGenre(genre);

        Pageable pageable = PageRequest.of(page, size);
        Page<BookResponseDTO> result = bookService.searchBooks(filters, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * Updates an existing book's information. Only librarians are authorized.
     */
    @Operation(summary = "Update book", description = "Updates an existing book by ID. Accessible by LIBRARIAN only.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{bookId}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long bookId,
                                                      @Valid @RequestBody BookRequestDTO bookRequestDTO) {
        log.info("Updating book with ID: {}", bookId);
        BookResponseDTO updatedBook = bookService.updateBook(bookId, bookRequestDTO);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Deletes a book by its ID. Only librarians are authorized.
     */
    @Operation(summary = "Delete book", description = "Deletes a book by ID. Accessible by LIBRARIAN only.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
        log.warn("Deleting book with ID: {}", bookId);
        bookService.deleteBook(bookId);
        return ResponseEntity.ok("Book Deleted Successfully");
    }
}
