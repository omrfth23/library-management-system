package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.request.BookRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.BookSearchRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BookResponseDTO;
import com.getir.librarymanagement.library_management_system.repository.BookRepository;
import com.getir.librarymanagement.library_management_system.service.impl.BookServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // application-test.properties
@AutoConfigureTestDatabase
class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    private BookRequestDTO createBookRequestDTO(String title, String isbn) {
        return BookRequestDTO.builder()
                .title(title)
                .author("Test Author")
                .isbn(isbn)
                .publicationDate(LocalDate.of(2020, 1, 1))
                .genre("Fiction")
                .quantity(5)
                .build();
    }

    @Test
    void testAddBook_success() {
        BookRequestDTO request = createBookRequestDTO("Sample Book", "ISBN123");

        BookResponseDTO response = bookService.addBook(request);

        assertNotNull(response);
        assertEquals("Sample Book", response.getTitle());
        assertEquals("ISBN123", response.getIsbn());
        assertEquals(5, response.getQuantity());
    }

    @Test
    void testGetBookById_success() {
        BookRequestDTO request = createBookRequestDTO("Get Book", "ISBN456");
        BookResponseDTO created = bookService.addBook(request);

        BookResponseDTO found = bookService.getBookById(created.getBookId());

        assertEquals(created.getTitle(), found.getTitle());
        assertEquals(created.getBookId(), found.getBookId());
    }

    @Test
    void testSearchBooks_success() {
        // given
        bookService.addBook(createBookRequestDTO("Searchable Book", "ISBN999"));
        bookService.addBook(createBookRequestDTO("Another Book", "ISBN888"));

        BookSearchRequestDTO filters = new BookSearchRequestDTO();
        filters.setTitle("Searchable");

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<BookResponseDTO> result = bookService.searchBooks(filters, pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("Searchable Book", result.getContent().get(0).getTitle());
    }

    @Test
    void testUpdateBook_success() {
        BookRequestDTO request = createBookRequestDTO("Old Title", "ISBN789");
        BookResponseDTO created = bookService.addBook(request);

        BookRequestDTO updateRequest = createBookRequestDTO("New Title", "ISBN789");
        updateRequest.setQuantity(10);

        BookResponseDTO updated = bookService.updateBook(created.getBookId(), updateRequest);

        assertEquals("New Title", updated.getTitle());
        assertEquals(10, updated.getQuantity());
    }

    @Test
    void testDeleteBook_success() {
        BookRequestDTO request = createBookRequestDTO("Delete Me", "ISBN000");
        BookResponseDTO created = bookService.addBook(request);

        String result = bookService.deleteBook(created.getBookId());

        assertEquals("Book Deleted Successfully!", result);
        assertFalse(bookRepository.findById(created.getBookId()).isPresent());
    }

    @Test
    void testGetAllBooks_success() {
        bookService.addBook(createBookRequestDTO("Book1", "ISBN001"));
        bookService.addBook(createBookRequestDTO("Book2", "ISBN002"));

        List<BookResponseDTO> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    void testGetBookById_notFound_shouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(9999L));
    }

    @Test
    void testUpdateBook_notFound_shouldThrowException() {
        BookRequestDTO updateRequest = createBookRequestDTO("Non-existent", "NOISBN");
        assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(9999L, updateRequest));
    }

    @Test
    void testDeleteBook_notFound_shouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBook(9999L));
    }

    @Test
    void testAddBook_duplicateISBN_shouldThrowException() {
        BookRequestDTO request = createBookRequestDTO("Book", "DUPLICATEISBN");
        bookService.addBook(request);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                bookService.addBook(createBookRequestDTO("Another Book", "DUPLICATEISBN"))
        );

        assertEquals("ISBN is already in use.", ex.getMessage());
    }
}
