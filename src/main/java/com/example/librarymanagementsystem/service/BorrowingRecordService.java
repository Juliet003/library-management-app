package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.dto.response.ApiResponse;
import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.entity.BorrowingRecord;
import org.springframework.stereotype.Service;

@Service
public interface BorrowingRecordService {

    ApiResponse<Book> borrowBook(Long bookId, String patronEmail);

    ApiResponse<String> returnBook(Long bookId, Long patronId);
}
