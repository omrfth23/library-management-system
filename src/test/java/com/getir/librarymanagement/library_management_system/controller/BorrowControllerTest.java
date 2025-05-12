package com.getir.librarymanagement.library_management_system.controller;

import com.getir.librarymanagement.library_management_system.model.dto.request.BorrowRequestDTO;
import com.getir.librarymanagement.library_management_system.model.dto.response.BorrowResponseDTO;
import com.getir.librarymanagement.library_management_system.service.IBorrowService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowControllerTest {

    @Mock
    private IBorrowService borrowService;

    @InjectMocks
    private BorrowController borrowController;

    @Test
    void testBorrowBook_success() {
        BorrowRequestDTO request = BorrowRequestDTO.builder()
                .bookId(1L)
                .borrowDate(LocalDate.now())
                .build();

        BorrowResponseDTO responseDTO = BorrowResponseDTO.builder()
                .borrowRecordId(1L)
                .bookId(1L)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(7))
                .isReturned(false)
                .build();

        when(borrowService.borrowBook(request)).thenReturn(responseDTO);

        ResponseEntity<BorrowResponseDTO> response = borrowController.borrowBook(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getBookId());
    }

    @Test
    void testReturnBook_success() {
        BorrowResponseDTO responseDTO = BorrowResponseDTO.builder()
                .borrowRecordId(1L)
                .bookId(1L)
                .isReturned(true)
                .returnDate(LocalDate.now())
                .build();

        when(borrowService.returnBook(1L)).thenReturn(responseDTO);

        ResponseEntity<BorrowResponseDTO> response = borrowController.returnBook(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getIsReturned());
    }

    @Test
    void testGetBorrowHistoryByUser_success() {
        List<BorrowResponseDTO> history = Collections.singletonList(
                BorrowResponseDTO.builder().borrowRecordId(1L).bookId(1L).build()
        );

        when(borrowService.getBorrowHistoryByUser(1L)).thenReturn(history);

        ResponseEntity<List<BorrowResponseDTO>> response = borrowController.getBorrowHistoryByUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetOverdueBooks_success() {
        List<BorrowResponseDTO> overdue = Collections.singletonList(
                BorrowResponseDTO.builder().borrowRecordId(1L).bookId(1L).dueDate(LocalDate.now().minusDays(1)).build()
        );

        when(borrowService.getOverdueBooks()).thenReturn(overdue);

        ResponseEntity<List<BorrowResponseDTO>> response = borrowController.getOverdueBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetOverdueReport_success() {
        String report = """
                LIBRARY MANAGEMENT - OVERDUE REPORT
                -----------------------------------
                Total Borrow Records: 3
                Overdue Books: 1
                Not Returned: 2
                Returned Books: 1
                
                Report Generated: 2025-05-10T14:30:00
                """;

        when(borrowService.generateOverdueReport()).thenReturn(report);

        ResponseEntity<String> response = borrowController.getOverdueReport();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Overdue Books: 1"));
        assertTrue(response.getBody().contains("Not Returned: 2"));
        assertTrue(response.getBody().contains("Returned Books: 1"));

        verify(borrowService, times(1)).generateOverdueReport();
    }

    @Test
    void testGetAllBorrowRecords_success() {
        List<BorrowResponseDTO> records = Collections.singletonList(
                BorrowResponseDTO.builder().borrowRecordId(1L).bookId(1L).build()
        );

        when(borrowService.getAllBorrowRecords()).thenReturn(records);

        ResponseEntity<List<BorrowResponseDTO>> response = borrowController.getAllBorrowRecords();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testDeleteBorrowRecord_success() {
        doNothing().when(borrowService).deleteBorrowRecord(1L);

        ResponseEntity<String> response = borrowController.deleteBorrowRecord(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Borrow Record deleted successfully.", response.getBody());
        verify(borrowService).deleteBorrowRecord(1L);
    }
}