package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.dto.request.LoginRequestDto;
import com.example.librarymanagementsystem.dto.request.RegistrationRequestDto;
import com.example.librarymanagementsystem.dto.response.ApiResponse;
import com.example.librarymanagementsystem.dto.response.LoginResponseDto;
import com.example.librarymanagementsystem.dto.response.RegistrationResponseDto;
import com.example.librarymanagementsystem.exception.PatronExistException;
import com.example.librarymanagementsystem.exception.PatronNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface PatronService {
    ApiResponse<RegistrationResponseDto> registerPatron(RegistrationRequestDto registrationRequest) throws PatronExistException;


    ApiResponse<LoginResponseDto> login(LoginRequestDto loginRequest);

    ApiResponse<String> updatePatron(RegistrationRequestDto requestDto, Long id) throws PatronNotFoundException;

    void deletePatron(String PatronEmail);

    ApiResponse<RegistrationResponseDto> getPatron(Long id);
}
