package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.model.dto.request.BookRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.BookSearchRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BookResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBookService {

    BookResponseDTO addBook(BookRequestDTO bookRequestDTO);

    BookResponseDTO getBookById(Long bookId);

    List<BookResponseDTO> getAllBooks();

    Page<BookResponseDTO> searchBooks(BookSearchRequestDTO filters, Pageable pageable);

    BookResponseDTO updateBook(Long bookId, BookRequestDTO bookRequestDTO);

    String deleteBook(Long bookId);
}
