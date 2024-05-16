package com.example.librarymanagementsystem.security.implementation;

import com.example.librarymanagementsystem.entity.Patron;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class PatronDetailsImpl implements UserDetails {
    private final Long id;
    private final String email;
    private final String fullName;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public PatronDetailsImpl(Patron patron) {
        this.id = patron.getId();
        this.email = patron.getEmail();
        this.fullName = patron.getFullName();
        this.password = patron.getPassword();
        this.authorities = Stream.of(new SimpleGrantedAuthority(patron.getRole().name()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
