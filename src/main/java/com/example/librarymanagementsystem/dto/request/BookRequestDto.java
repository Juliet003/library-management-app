package com.example.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequestDto {
    private Long id;
    @NotBlank(message = "Book title is mandatory")
    private String title;
    @NotNull(message = "Book author is mandatory")
    private String author;
    @Min(value = 1000, message = "Publication year must be after 999")
    private int publicationYear;
    @NotBlank(message = "ISBN is mandatory")
    private String isbn;
}
