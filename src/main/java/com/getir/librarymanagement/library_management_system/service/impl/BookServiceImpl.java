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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements IBookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    @Override
    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO) {
        if (bookRepository.existsByIsbn(bookRequestDTO.getIsbn())) {
            throw new IllegalArgumentException("ISBN is already in use.");
        }

        Book book = bookMapper.toEntity(bookRequestDTO);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public BookResponseDTO getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookResponseDTO> searchBooks(BookSearchRequestDTO filters, Pageable pageable) {
        Page<Book> books = bookRepository.findAll(BookSpecification.withFilters(filters), pageable);
        return books.map(bookMapper::toDto);
    }

    @Override
    public BookResponseDTO updateBook(Long bookId, BookRequestDTO bookRequestDTO) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));

        bookMapper.updateBookFromDto(bookRequestDTO, book);

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public String deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Book not found with id: " + bookId);
        }
        bookRepository.deleteById(bookId);
        return "Book Deleted Succesfully!";
    }
}
