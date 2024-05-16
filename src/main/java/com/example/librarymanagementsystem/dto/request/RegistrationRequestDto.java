package com.example.librarymanagementsystem.dto.request;

import com.example.librarymanagementsystem.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
public record RegistrationRequestDto(
        @NotNull(message = "FirstName is required")
        String fullName,

        @Email(message = "Must match a proper email")
        @NotNull(message = "Email is required")
        String email,
        String contactInformation,
        String address,
        @Size(message = "Password must not be less than 6", min = 6)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,20}$", message = "Password must contain at least one lowercase letter, one uppercase letter, and one number")
        String password,
        Role role

){}
