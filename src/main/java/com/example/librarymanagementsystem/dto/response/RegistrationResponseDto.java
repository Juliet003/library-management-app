package com.example.librarymanagementsystem.dto.response;


import com.example.librarymanagementsystem.enums.Role;
import lombok.Builder;

@Builder
public record RegistrationResponseDto(
        String fullName,
        String email,
        String contactInformation,
        String address,
        Role role
) {
}
