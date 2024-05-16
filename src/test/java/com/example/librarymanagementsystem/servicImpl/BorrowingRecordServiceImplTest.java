package com.example.librarymanagementsystem.servicImpl;

import com.example.librarymanagementsystem.dto.response.ApiResponse;
import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.entity.BorrowingRecord;
import com.example.librarymanagementsystem.entity.Patron;
import com.example.librarymanagementsystem.exception.BookNotFoundException;
import com.example.librarymanagementsystem.exception.BorrowingRecordNotFoundException;
import com.example.librarymanagementsystem.exception.PatronNotFoundException;
import com.example.librarymanagementsystem.repository.BookRepository;
import com.example.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.example.librarymanagementsystem.repository.PatronRepository;
import com.example.librarymanagementsystem.utils.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BorrowingRecordServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private DtoMapper dtoMapper;

    @InjectMocks
    private BorrowingRecordServiceImpl borrowingRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(patronRepository.findByEmail(anyString())).thenReturn(Optional.of(new Patron()));
    }

    @Test
    void borrowBook_success() {
        
        Long bookId = 1L;
        String patronEmail = "email@example.com";
        Book book = new Book();
        Patron patron = new Patron();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findByEmail(patronEmail)).thenReturn(Optional.of(patron));

        
        ApiResponse<Book> response = borrowingRecordService.borrowBook(bookId, patronEmail);

        
        assertEquals("Borrow book successful", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void borrowBook_bookNotFound() {
        
        Long bookId = 1L;
        String patronEmail = "email@example.com";
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        
        assertThrows(BookNotFoundException.class, () -> borrowingRecordService.borrowBook(bookId, patronEmail));
    }

    @Test
    void borrowBook_patronNotFound() {
        
        Long bookId = 1L;
        String patronEmail = "email@example.com";
        Book book = new Book();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findByEmail(patronEmail)).thenReturn(Optional.empty());

        
        assertThrows(PatronNotFoundException.class, () -> borrowingRecordService.borrowBook(bookId, patronEmail));
    }

    @Test
    void returnBook_success() {
        
        Long bookId = 1L;
        Long patronId = 1L;
        BorrowingRecord borrowing = new BorrowingRecord();
        when(borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId)).thenReturn(Optional.of(borrowing));
        
        ApiResponse<String> response = borrowingRecordService.returnBook(bookId, patronId);

        assertEquals("Returned book recorded", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void returnBook_borrowingRecordNotFound() {
        
        Long bookId = 1L;
        Long patronId = 1L;
        when(borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId)).thenReturn(Optional.empty());

        
        assertThrows(BorrowingRecordNotFoundException.class, () -> borrowingRecordService.returnBook(bookId, patronId));
    }
}