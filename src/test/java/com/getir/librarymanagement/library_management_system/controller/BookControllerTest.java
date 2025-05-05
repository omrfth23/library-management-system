package com.getir.librarymanagement.library_management_system.controller;

import com.getir.librarymanagement.library_management_system.model.dto.request.BookRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BookResponseDTO;
import com.getir.librarymanagement.library_management_system.service.IBookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private IBookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    void testAddBook_success() {
        BookRequestDTO request = new BookRequestDTO("Title", "Author", "1234567890", LocalDate.now(), "Fiction", 3);
        BookResponseDTO responseDTO = new BookResponseDTO(1L, "Title", "Author", "1234567890", LocalDate.now(), "Fiction", 3, true);

        when(bookService.addBook(request)).thenReturn(responseDTO);

        ResponseEntity<BookResponseDTO> response = bookController.addBook(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Title", response.getBody().getTitle());
    }

    @Test
    void testGetBookById_success() {
        BookResponseDTO responseDTO = new BookResponseDTO(1L, "Title", "Author", "1234567890", LocalDate.now(), "Fiction", 3, true);

        when(bookService.getBookById(1L)).thenReturn(responseDTO);

        ResponseEntity<BookResponseDTO> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Title", response.getBody().getTitle());
    }

    @Test
    void testGetAllBooks_success() {
        List<BookResponseDTO> books = Collections.singletonList(
                new BookResponseDTO(1L, "Title", "Author", "1234567890", LocalDate.now(), "Fiction", 3, true)
        );
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<BookResponseDTO>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testSearchBooks_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookResponseDTO> page = new PageImpl<>(Collections.singletonList(
                new BookResponseDTO(1L, "Title", "Author", "1234567890", LocalDate.now(), "Fiction", 3, true)
        ));

        when(bookService.searchBooks(any(), eq(pageable))).thenReturn(page);

        ResponseEntity<Page<BookResponseDTO>> response = bookController.searchBooks("Title", null, null, null, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testUpdateBook_success() {
        BookRequestDTO request = new BookRequestDTO("Updated Title", "Updated Author", "0987654321", LocalDate.now(), "Drama", 5);
        BookResponseDTO responseDTO = new BookResponseDTO(1L, "Updated Title", "Updated Author", "0987654321", LocalDate.now(), "Drama", 5, true);

        when(bookService.updateBook(1L, request)).thenReturn(responseDTO);

        ResponseEntity<BookResponseDTO> response = bookController.updateBook(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Title", response.getBody().getTitle());
    }

    @Test
    void testDeleteBook_success() {
        when(bookService.deleteBook(1L)).thenReturn("Book deleted successfully!");

        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService).deleteBook(1L);
    }
}
