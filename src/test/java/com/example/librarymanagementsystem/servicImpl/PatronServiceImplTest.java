package com.example.librarymanagementsystem.servicImpl;

import com.example.librarymanagementsystem.dto.request.LoginRequestDto;
import com.example.librarymanagementsystem.dto.request.RegistrationRequestDto;
import com.example.librarymanagementsystem.dto.response.ApiResponse;
import com.example.librarymanagementsystem.dto.response.LoginResponseDto;
import com.example.librarymanagementsystem.dto.response.RegistrationResponseDto;
import com.example.librarymanagementsystem.entity.Patron;
import com.example.librarymanagementsystem.enums.Role;
import com.example.librarymanagementsystem.exception.InvalidCredentialException;
import com.example.librarymanagementsystem.exception.PatronExistException;
import com.example.librarymanagementsystem.exception.PatronNotFoundException;
import com.example.librarymanagementsystem.repository.PatronRepository;
import com.example.librarymanagementsystem.security.JwtService;
import com.example.librarymanagementsystem.security.implementation.PatronDetailsImpl;
import com.example.librarymanagementsystem.utils.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

public class PatronServiceImplTest {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private DtoMapper dtoMapper;

    @InjectMocks
    private PatronServiceImpl patronService;

    private RegistrationRequestDto registrationRequestDto;
    private LoginRequestDto loginRequestDto;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(patronRepository.findByEmail(anyString())).thenReturn(Optional.of(new Patron()));
        registrationRequestDto = RegistrationRequestDto.builder()
                .fullName("John Doe")
                .email("john@example.com")
                .contactInformation("1234567890")
                .address("123 Main St")
                .password("Password123")
                .role(Role.PATRON)
                .build();
        loginRequestDto = LoginRequestDto.builder()
                .email("john@example.com")
                .password("Password123")
                .build();
    }



    @Test
    void registerPatron_success() {
        Patron newPatron = new Patron();
        when(patronRepository.findByEmail(registrationRequestDto.email())).thenReturn(Optional.empty());
        when(dtoMapper.createNewPatron(registrationRequestDto)).thenReturn(newPatron);
        when(patronRepository.save(newPatron)).thenReturn(newPatron);

        ApiResponse<RegistrationResponseDto> response = patronService.registerPatron(registrationRequestDto);

        assertEquals("Patron created successfully", response.getMessage());
        assertEquals(CREATED, response.getHttpStatus());
    }

    @Test
    void registerPatron_patronExistException() {
        when(patronRepository.findByEmail(registrationRequestDto.email())).thenReturn(Optional.of(new Patron()));

        assertThrows(PatronExistException.class, () -> patronService.registerPatron(registrationRequestDto));
    }


    @Test
    void login_invalidCredentials() {
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password()))).thenThrow(new BadCredentialsException("Invalid credentials"));
        assertThrows(InvalidCredentialException.class, () -> patronService.login(loginRequestDto));
    }
}