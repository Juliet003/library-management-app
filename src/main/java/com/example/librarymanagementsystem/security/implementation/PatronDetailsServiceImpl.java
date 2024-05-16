package com.example.librarymanagementsystem.security.implementation;

import com.example.librarymanagementsystem.exception.PatronNotFoundException;
import com.example.librarymanagementsystem.repository.PatronRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@ToString
public class PatronDetailsServiceImpl implements UserDetailsService {

    private final PatronRepository patronRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws PatronNotFoundException {
        return patronRepository.findByEmail(username)
                .map(PatronDetailsImpl::new)
                .orElseThrow(() -> new PatronNotFoundException("No active user found with email: " + username));
    }
}
