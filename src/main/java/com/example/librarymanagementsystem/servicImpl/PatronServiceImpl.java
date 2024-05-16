package com.example.librarymanagementsystem.servicImpl;

import com.example.librarymanagementsystem.dto.request.LoginRequestDto;
import com.example.librarymanagementsystem.dto.request.RegistrationRequestDto;
import com.example.librarymanagementsystem.dto.response.ApiResponse;
import com.example.librarymanagementsystem.dto.response.LoginResponseDto;
import com.example.librarymanagementsystem.dto.response.RegistrationResponseDto;
import com.example.librarymanagementsystem.entity.Patron;
import com.example.librarymanagementsystem.enums.Role;
import com.example.librarymanagementsystem.exception.*;
import com.example.librarymanagementsystem.repository.PatronRepository;
import com.example.librarymanagementsystem.security.JwtService;
import com.example.librarymanagementsystem.security.implementation.PatronDetailsImpl;
import com.example.librarymanagementsystem.service.PatronService;
import com.example.librarymanagementsystem.utils.DtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.librarymanagementsystem.enums.Role.LIBARIAN;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PatronServiceImpl implements PatronService {
    private final PatronRepository patronRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final DtoMapper dtoMapper;

    @Override
    public ApiResponse<RegistrationResponseDto> registerPatron(RegistrationRequestDto registrationRequest) throws PatronExistException {
        Optional<Patron> optionalPatron = patronRepository.findByEmail(registrationRequest.email());
        if (optionalPatron.isPresent()) {
            throw new PatronExistException("Patron Already Exists");
        }
        Patron newPatron = dtoMapper.createNewPatron(registrationRequest);
        Patron savedPatron = patronRepository.save(newPatron);
        RegistrationResponseDto registrationResponse = dtoMapper.createPatronResponse(savedPatron);
        return new ApiResponse<>("Patron created successfully", "Success", CREATED, registrationResponse);
    }

    @Transactional
    @Override
    public ApiResponse<LoginResponseDto> login(LoginRequestDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
                    );

            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                PatronDetailsImpl patronDetails = (PatronDetailsImpl) authentication.getPrincipal();

                Role patronRole = extractPatronRole(patronDetails);

                String token = jwtService.generateToken(authentication, patronRole);
                LoginResponseDto loginResponse = dtoMapper.createLoginResponse(patronDetails, token);

                return new ApiResponse<>("Successfully logged in", "Success", OK, loginResponse);
            } else
                return new ApiResponse<>("Logged in failed", null, NETWORK_AUTHENTICATION_REQUIRED);

        } catch (DisabledException e) {
            throw new RuntimeException("Patron is disabled");
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialException("Invalid credentials: Enter email and password again");
        }
    }

    private Role extractPatronRole(UserDetails patronDetails) {
        if (patronDetails == null) {
            throw new IllegalArgumentException("Invalid entry: retry again");
        }

        if (patronDetails instanceof PatronDetailsImpl patronDetailsImpl) {
            return Role.valueOf(
                    patronDetailsImpl.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse(null)
            );
        } else {
            Collection<? extends GrantedAuthority> authorities = patronDetails.getAuthorities();
            if (authorities.isEmpty()) {
                throw new IllegalStateException("patron has no authorities");
            }
            GrantedAuthority authority = authorities.iterator().next();
            String roleString = authority.getAuthority();

            try {
                return Role.valueOf(roleString);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Invalid patron role: " + roleString);
            }
        }
    }

    @CachePut(value = "patrons", key = "#requestDto")
    @Override
    public ApiResponse<String> updatePatron(RegistrationRequestDto requestDto, Long id) throws PatronNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new PatronNotAuthenticatedException("Authentication required.");
        }

        Optional<Patron> optionalPatron = patronRepository.findById(id);

        if (optionalPatron.isEmpty()) {
            throw new PatronNotFoundException("Patron not found.");
        }
        Patron patron = optionalPatron.get();
        patron.setFullName(requestDto.fullName());
        patron.setContactInformation(requestDto.contactInformation());
        patron.setAddress(requestDto.address());
        patronRepository.save(patron);
        log.info("Patron Updated Successfully");
        return new ApiResponse<>("Patron profile updated successfully.");
    }

    @CachePut(value = "patrons")
    @Override
    public void deletePatron(String patronEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new PatronNotAuthenticatedException("Authentication required.");
        }
        UserDetails authenticatedPatron = (UserDetails) authentication.getPrincipal();
        Set<String> roles = authenticatedPatron.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (!roles.contains(LIBARIAN.name())) {
            throw new NotAuthorizedException("You are not authorized to delete this resources.");
        }

        Optional<Patron> optionalPatron = patronRepository.findByEmail(patronEmail);

        if (optionalPatron.isEmpty()) {
            throw new PatronNotFoundException("Patron not found.");
        } else {
            patronRepository.deleteById(optionalPatron.get().getId());
            log.info("Patron Updated Successfully");
        }
    }

    @Cacheable(value = "patrons", key = "#id")
    @Override
    public ApiResponse<RegistrationResponseDto> getPatron(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new PatronNotAuthenticatedException("Authentication required.");
        }
        Patron optionalPatron = patronRepository.findById(id).orElseThrow(() ->
                new PatronNotFoundException("Patron not found."));
        RegistrationResponseDto registrationResponseDto = dtoMapper.createPatronResponse(optionalPatron);
        log.info("Patron Retrieved Successfully");
        return new ApiResponse<>("Patron profile successfully retrieved.", "Successful", OK, registrationResponseDto);
    }
}
