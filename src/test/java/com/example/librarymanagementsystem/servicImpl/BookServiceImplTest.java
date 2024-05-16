package com.example.librarymanagementsystem.servicImpl;

import com.example.librarymanagementsystem.dto.request.BookRequestDto;
import com.example.librarymanagementsystem.dto.response.ApiResponse;
import com.example.librarymanagementsystem.dto.response.BookResponseDto;
import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.entity.Patron;
import com.example.librarymanagementsystem.enums.Role;
import com.example.librarymanagementsystem.exception.NotAuthorizedException;
import com.example.librarymanagementsystem.repository.BookRepository;
import com.example.librarymanagementsystem.repository.PatronRepository;
import com.example.librarymanagementsystem.utils.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private DtoMapper dtoMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(patronRepository.findByEmail(anyString())).thenReturn(Optional.of(new Patron()));
    }

    @Test
    void testGetAllBooks() {
        
        List<Book> books = new ArrayList<>();
        when(bookRepository.findAll()).thenReturn(books);

        ApiResponse<List<BookResponseDto>> response = bookService.getAllBooks("test@example.com");

        assertEquals("All Books retrieved successfully", response.getMessage());
        assertEquals("success", response.getStatus());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(books.size(), response.getData().size());
        verify(patronRepository, times(1)).findByEmail(anyString());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById_BookFound() {
        
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        
        ApiResponse<BookResponseDto> response = bookService.getBookById(1L, "test@example.com");

        
        assertEquals("Book retrieved successfully", response.getMessage());
        assertEquals("success", response.getStatus());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getData());
        verify(patronRepository, times(1)).findByEmail(anyString());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetBookById_BookNotFound() {
        ApiResponse<BookResponseDto> response = bookService.getBookById(1L, "test@example.com");
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        assertEquals("Book not found", response.getMessage());
        assertEquals("failed", response.getStatus());
        assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
        assertNull(response.getData());
        verify(patronRepository, times(1)).findByEmail(anyString());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllBooks_success() {
        
        String email = "email@example.com";
        List<Book> books = List.of(new Book());
        when(bookRepository.findAll()).thenReturn(books);

        
        ApiResponse<List<BookResponseDto>> response = bookService.getAllBooks(email);

        
        assertEquals("All Books retrieved successfully", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void getBookById_success() {
        
        Long id = 1L;
        String email = "email@example.com";
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        
        ApiResponse<BookResponseDto> response = bookService.getBookById(id, email);

        
        assertEquals("Book retrieved successfully", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void updateBook_success() {
        
        BookRequestDto bookDto = new BookRequestDto();
        String email = "email@example.com";
        Long id = 1L;
        Patron patron = new Patron();
        patron.setRole(Role.LIBARIAN);
        when(patronRepository.findByEmail(email)).thenReturn(Optional.of(patron));
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        Book updatedBook = new Book();
        when(bookRepository.save(book)).thenReturn(updatedBook);

        
        ApiResponse<BookResponseDto> response = bookService.updateBook(bookDto, email, id);

        
        assertEquals("Book updated successfully", response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void deleteBook_success() {
        
        Long id = 1L;
        String email = "email@example.com";
        Patron patron = new Patron();
        patron.setRole(Role.LIBARIAN);
        when(patronRepository.findByEmail(email)).thenReturn(Optional.of(patron));

        
        bookService.deleteBook(id, email);
    }

    @Test
    void findPatronByEmail_success() {
        String email = "email@example.com";
        Patron patron = new Patron();
        when(patronRepository.findByEmail(email)).thenReturn(Optional.of(patron));
        Patron foundPatron = bookService.findPatronByEmail(email);

        assertEquals(patron, foundPatron);
    }

    @Test
    void createBook_notAuthorized() {
        BookRequestDto bookDto = new BookRequestDto();
        String email = "email@example.com";
        Patron patron = new Patron();
        patron.setRole(Role.PATRON);
        when(patronRepository.findByEmail(email)).thenReturn(Optional.of(patron));

        assertThrows(NotAuthorizedException.class, () -> bookService.createBook(bookDto, email));
    }

    @Test
    void updateBook_notAuthorized() {
        BookRequestDto bookDto = new BookRequestDto();
        String email = "email@example.com";
        Long id = 1L;
        Patron patron = new Patron();
        patron.setRole(Role.PATRON);
        when(patronRepository.findByEmail(email)).thenReturn(Optional.of(patron));
        assertThrows(NotAuthorizedException.class, () -> bookService.updateBook(bookDto, email, id));
    }
}
