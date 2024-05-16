package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.dto.response.ApiResponse;
import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.entity.BorrowingRecord;
import com.example.librarymanagementsystem.security.JwtService;
import com.example.librarymanagementsystem.service.BorrowingRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class BorrowingController {

    private final BorrowingRecordService borrowingRecordService;
    private final JwtService jwtService;

    @PostMapping("/api/borrow/{bookId}")
    public ResponseEntity<ApiResponse<Book>> borrowBook(@PathVariable Long bookId, @RequestHeader("Authorization") String authorizationHeader) {
        var userDetails = jwtService.parseTokenClaims(authorizationHeader.substring(7));
        ApiResponse<Book> response = borrowingRecordService.borrowBook(bookId, userDetails.get("email"));
        return ResponseEntity.status(OK).body(response);
    }

    @PutMapping("/api/return/{bookId}")
    public ResponseEntity<ApiResponse<String>> returnBook(@PathVariable Long bookId, @RequestParam Long patronId) {
        ApiResponse<String> response =  borrowingRecordService.returnBook(bookId, patronId);
        return ResponseEntity.status(OK).body(response);
    }
}