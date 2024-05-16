package com.example.librarymanagementsystem.servicImpl;

import com.example.librarymanagementsystem.dto.request.BookRequestDto;
import com.example.librarymanagementsystem.dto.response.ApiResponse;
import com.example.librarymanagementsystem.dto.response.BookResponseDto;
import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.entity.Patron;
import com.example.librarymanagementsystem.enums.Role;
import com.example.librarymanagementsystem.exception.BookAlreadyExistException;
import com.example.librarymanagementsystem.exception.BookNotFoundException;
import com.example.librarymanagementsystem.exception.NotAuthorizedException;
import com.example.librarymanagementsystem.exception.PatronNotFoundException;
import com.example.librarymanagementsystem.repository.BookRepository;
import com.example.librarymanagementsystem.repository.PatronRepository;
import com.example.librarymanagementsystem.service.BookService;
import com.example.librarymanagementsystem.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final DtoMapper dtoMapper;

    @Cacheable(value = "books")
    @Override
    public ApiResponse<List<BookResponseDto>> getAllBooks(String email) {
        findPatronByEmail(email);
        List<Book> books = bookRepository.findAll();
        List<BookResponseDto> bookResponseDto = books.stream()
                .map(DtoMapper::createBookResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>("All Books retrieved successfully", "success", OK, bookResponseDto);
    }
    @Cacheable(value = "books", key = "#id")
    @Override
    public ApiResponse<BookResponseDto> getBookById(Long id, String email) {
        findPatronByEmail(email);
        Optional<Book> optionalBook = bookRepository.findById(id);
        return optionalBook.map(Book ->
                new ApiResponse<>("Book retrieved successfully", "success",
                        OK, DtoMapper.createBookResponse(Book)))
                .orElseGet(() -> new ApiResponse<>("Book not found", "failed", NOT_FOUND));
    }


    @Override
    @CachePut(value = "books", key = "#bookDto")
    public ApiResponse<BookResponseDto> createBook(BookRequestDto bookDto, String email) {
        Patron retrievedPatron = findPatronByEmail(email);

        if (retrievedPatron.getRole() != Role.LIBARIAN) {
            throw new NotAuthorizedException("You are not authorized create book.");
        }

        Book existingBook = findBookByTitle(bookDto.getTitle());
        if (existingBook != null) {
            throw new BookAlreadyExistException("Book already exists with title: " + bookDto.getTitle());
        }

        Book newBook = dtoMapper.createNewBook(bookDto);
        Book savedBook = bookRepository.save(newBook);

        return new ApiResponse<>("Book created successfully", "success", HttpStatus.CREATED, DtoMapper.createBookResponse(savedBook));
    }

    private Book findBookByTitle(String title) {
        return bookRepository.findBookByTitle(title);
    }

    @CachePut(value = "books", key = "#bookDto + '_' + #id")
    @Override
    public ApiResponse<BookResponseDto> updateBook(BookRequestDto bookDto, String email, Long id) {
        Patron retreivedPatron = findPatronByEmail(email);
        if (!(retreivedPatron.getRole() == Role.LIBARIAN)) {
            throw new NotAuthorizedException("You are not authorized to update book.");
        } else {
            Book retrivedBook = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
                retrivedBook.setTitle(bookDto.getTitle());
                retrivedBook.setIsbn(bookDto.getIsbn());
                retrivedBook.setAuthor(bookDto.getAuthor());
                retrivedBook.setPublicationYear(bookDto.getPublicationYear());
            Book updatedBook = bookRepository.save(retrivedBook);
            return new ApiResponse<>("Book updated successfully", "success", OK, DtoMapper.createBookResponse(updatedBook));
        }
    }
    @CachePut(value = "books")
    @Override
    public void deleteBook(Long id, String email) {
        Patron retreivedPatron = findPatronByEmail(email);
        if (!(retreivedPatron.getRole() == Role.LIBARIAN)) {
            throw new NotAuthorizedException("You are not authorized to delete this book.");
        } else {
            bookRepository.deleteById(id);
        }
    }
    @Cacheable(value = "books", key = "#email")
    public Patron findPatronByEmail(String email) {
        return patronRepository.findByEmail(email).orElseThrow(() ->
                new PatronNotFoundException("Patron not found"));
    }


}
