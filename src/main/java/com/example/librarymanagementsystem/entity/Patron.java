package com.example.librarymanagementsystem.entity;


import com.example.librarymanagementsystem.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Patron {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full-name",nullable = false)
    private String fullName;

    @Column(name = "contact-information",nullable = false)
    private String contactInformation;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "address",nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;
}
