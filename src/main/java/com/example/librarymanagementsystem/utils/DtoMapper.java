package com.example.librarymanagementsystem.utils;
import com.example.librarymanagementsystem.dto.request.BookRequestDto;
import com.example.librarymanagementsystem.dto.request.RegistrationRequestDto;
import com.example.librarymanagementsystem.dto.response.BookResponseDto;
import com.example.librarymanagementsystem.dto.response.LoginResponseDto;
import com.example.librarymanagementsystem.dto.response.RegistrationResponseDto;
import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.entity.Patron;
import com.example.librarymanagementsystem.security.implementation.PatronDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DtoMapper {
    private final PasswordEncoder passwordEncoder;



    public Patron createNewPatron(RegistrationRequestDto newPatron) {

        return Patron.builder()
                .fullName(newPatron.fullName())
                .email(newPatron.email())
                .contactInformation(newPatron.contactInformation())
                .address(newPatron.address())
                .password(passwordEncoder.encode(newPatron.password()))
                .role(newPatron.role())
                .build();
    }

    public RegistrationResponseDto createPatronResponse(Patron savedPatron) {
        return RegistrationResponseDto.builder()
                .fullName(savedPatron.getFullName())
                .email(savedPatron.getEmail())
                .contactInformation(savedPatron.getContactInformation())
                .address(savedPatron.getAddress())
                .role(savedPatron.getRole())
                .build();
    }

    public LoginResponseDto createLoginResponse(PatronDetailsImpl savedPatron, String token) {
        return LoginResponseDto.builder()
                .id(savedPatron.getId())
                .fullName(savedPatron.getFullName())
                .email(savedPatron.getEmail())
                .jwtToken(token)
                .build();
    }

    public Book createNewBook(BookRequestDto newBook) {

        return Book.builder()
                .title(newBook.getTitle())
                .isbn(newBook.getIsbn())
                .publicationYear(newBook.getPublicationYear())
                .author(newBook.getAuthor())
                .id(newBook.getId())
                .build();
    }

        public static BookResponseDto createBookResponse (Book newBook){

            return BookResponseDto.builder()
                    .title(newBook.getTitle())
                    .isbn(newBook.getIsbn())
                    .publicationYear(newBook.getPublicationYear())
                    .author(newBook.getAuthor())
                    .id(newBook.getId())
                    .build();
        }
    }
