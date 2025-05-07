package com.getir.librarymanagement.library_management_system.service;

import com.getir.librarymanagement.library_management_system.enums.Role;
import com.getir.librarymanagement.library_management_system.model.dto.request.BookRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.BorrowRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.request.UserRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BorrowResponseDTO;
import com.getir.librarymanagement.library_management_system.repository.BookRepository;
import com.getir.librarymanagement.library_management_system.repository.BorrowRecordRepository;
import com.getir.librarymanagement.library_management_system.repository.UserRepository;
import com.getir.librarymanagement.library_management_system.service.impl.BorrowServiceImpl;
import com.getir.librarymanagement.library_management_system.service.impl.UserServiceImpl;
import com.getir.librarymanagement.library_management_system.service.impl.BookServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // application-test.properties
@AutoConfigureTestDatabase
class BorrowServiceTest {

    @Autowired
    private BorrowServiceImpl borrowService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    private Long userId;
    private Long bookId;

    @BeforeEach
    void setUp() {
        // sahte kullanıcı oturumu oluştur
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("borrow@example.com", null, List.of());

        SecurityContextHolder.getContext().setAuthentication(auth);

        // kullanıcı ve kitap oluşturma (önceki kod)
        borrowRecordRepository.deleteAll();
        userRepository.deleteAll();
        bookRepository.deleteAll();

        userService.createUser(UserRequestDTO.builder()
                .name("Borrow Test User")
                .email("borrow@example.com") // aynı e-posta burada da
                .password("password")
                .phone("123456789")
                .role(Role.LIBRARIAN)
                .build());

        bookId = bookService.addBook(BookRequestDTO.builder()
                .title("Borrowable Book")
                .author("Author")
                .isbn("ISBN-BORROW")
                .publicationDate(LocalDate.of(2022, 1, 1))
                .genre("Fiction")
                .quantity(3)
                .build()).getBookId();
    }

    @Test
    void testBorrowBook_success() {
        BorrowRequestDTO borrowRequest = BorrowRequestDTO.builder()
                .bookId(bookId)
                .borrowDate(LocalDate.now())
                .build();

        BorrowResponseDTO response = borrowService.borrowBook(borrowRequest);

        assertNotNull(response);
        assertEquals(bookId, response.getBookId());
        assertFalse(response.getIsReturned());

        assertNotNull(response.getUserId());
    }

    @Test
    void testReturnBook_success() {
        BorrowRequestDTO borrowRequest = BorrowRequestDTO.builder()
                .bookId(bookId)
                .borrowDate(LocalDate.now().minusDays(3))
                .build();

        BorrowResponseDTO borrowRecord = borrowService.borrowBook(borrowRequest);

        BorrowResponseDTO returned = borrowService.returnBook(borrowRecord.getBorrowRecordId());

        assertTrue(returned.getIsReturned());
        assertNotNull(returned.getReturnDate());
    }

    @Test
    void testGetBorrowHistoryByUser_success() {
        BorrowRequestDTO borrowRequest = BorrowRequestDTO.builder()
                .bookId(bookId)
                .borrowDate(LocalDate.now())
                .build();

        borrowService.borrowBook(borrowRequest);

        Long userId = userRepository.findByEmail("borrow@example.com")
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        List<BorrowResponseDTO> history = borrowService.getBorrowHistoryByUser(userId);

        assertEquals(1, history.size());
        assertEquals(userId, history.get(0).getUserId());
    }

    @Test
    void testGetAllBorrowRecords_success() {
        borrowService.borrowBook(BorrowRequestDTO.builder()
                .bookId(bookId)
                .borrowDate(LocalDate.now())
                .build());

        List<BorrowResponseDTO> records = borrowService.getAllBorrowRecords();

        assertFalse(records.isEmpty());
    }

    @Test
    void testGetOverdueBooks_shouldReturnList() {
        // overdue record
        borrowService.borrowBook(BorrowRequestDTO.builder()
                .bookId(bookId)
                .borrowDate(LocalDate.now().minusDays(15))
                .build());

        List<BorrowResponseDTO> overdue = borrowService.getOverdueBooks();

        assertEquals(1, overdue.size());
        assertTrue(overdue.get(0).getDueDate().isBefore(LocalDate.now()));
    }

    @Test
    void testDeleteBorrowRecord_success() {

        BorrowRequestDTO borrowRequest = BorrowRequestDTO.builder()
                .bookId(bookId)
                .borrowDate(LocalDate.now())
                .build();

        BorrowResponseDTO response = borrowService.borrowBook(borrowRequest);

        Long borrowRecordId = response.getBorrowRecordId();


        borrowService.deleteBorrowRecord(borrowRecordId);


        boolean exists = borrowRecordRepository.findById(borrowRecordId).isPresent();

        assertFalse(exists, "Borrow record should be deleted");
    }

    @Test
    void testBorrowBook_withNonexistentBook_shouldThrowException() {
        BorrowRequestDTO invalidRequest = BorrowRequestDTO.builder()
                .bookId(9999L)
                .borrowDate(LocalDate.now())
                .build();

        assertThrows(EntityNotFoundException.class, () -> borrowService.borrowBook(invalidRequest));
    }

    @Test
    void testReturnBook_withInvalidId_shouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> borrowService.returnBook(9999L));
    }

    @Test
    void testDeleteBorrowRecord_withInvalidId_shouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> borrowService.deleteBorrowRecord(9999L));
    }

    @Test
    void testGetBorrowHistoryByUser_withInvalidUserId_shouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> borrowService.getBorrowHistoryByUser(9999L));
    }
}