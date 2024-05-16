package com.example.librarymanagementsystem.service;


import com.example.librarymanagementsystem.dto.request.BookRequestDto;
import com.example.librarymanagementsystem.dto.response.ApiResponse;
import com.example.librarymanagementsystem.dto.response.BookResponseDto;

import java.util.List;

public interface BookService {


    ApiResponse<List<BookResponseDto>> getAllBooks(String email);

    ApiResponse<BookResponseDto> getBookById(Long id, String email);

    ApiResponse<BookResponseDto> createBook(BookRequestDto bookDto, String email);

    ApiResponse<BookResponseDto> updateBook(BookRequestDto bookDto, String email, Long id);

    void deleteBook(Long id, String email);
}