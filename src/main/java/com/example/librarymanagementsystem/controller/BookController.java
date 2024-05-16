package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.dto.request.BookRequestDto;
import com.example.librarymanagementsystem.dto.response.ApiResponse;
import com.example.librarymanagementsystem.dto.response.BookResponseDto;
import com.example.librarymanagementsystem.security.JwtService;
import com.example.librarymanagementsystem.servicImpl.BookServiceImpl;
import com.example.librarymanagementsystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BookController {


    private final BookService bookService;
    private final JwtService jwtService;

    @GetMapping("/get-all-books")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getAllBooks(@RequestHeader("Authorization") String authorizationHeader) {
        var userDetails = jwtService.parseTokenClaims(authorizationHeader.substring(7));
        ApiResponse<List<BookResponseDto>> response = bookService.getAllBooks(userDetails.get("email"));
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/get-book-by-id/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> getBookById(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        var userDetails = jwtService.parseTokenClaims(authorizationHeader.substring(7));
        ApiResponse<BookResponseDto> response = bookService.getBookById(id, userDetails.get("email"));
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/create-book")
    public ResponseEntity<ApiResponse<BookResponseDto>> createBook(@RequestBody BookRequestDto bookDto, @RequestHeader("Authorization") String authorizationHeader) {
        var userDetails = jwtService.parseTokenClaims(authorizationHeader.substring(7));
        ApiResponse<BookResponseDto> response = bookService.createBook(bookDto, userDetails.get("email"));
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update-book/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> updateBook(@RequestBody BookRequestDto bookDto,  @RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
        var userDetails = jwtService.parseTokenClaims(authorizationHeader.substring(7));
        ApiResponse<BookResponseDto> response = bookService.updateBook(bookDto, userDetails.get("email"), id);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete-book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        var userDetails = jwtService.parseTokenClaims(authorizationHeader.substring(7));
        bookService.deleteBook(id, userDetails.get("email"));
        return ResponseEntity.noContent().build();
    }
}
