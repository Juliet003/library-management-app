package com.example.librarymanagementsystem.security;
import com.example.librarymanagementsystem.enums.Role;
import com.example.librarymanagementsystem.exception.JwtParsingException;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface JwtService {

    String generateToken(Authentication authentication, Role role);
    Map<String, String> parseTokenClaims(String token) throws JwtParsingException;
}
