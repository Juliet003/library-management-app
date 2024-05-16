package com.example.librarymanagementsystem.servicImpl;

import com.example.librarymanagementsystem.dto.response.ApiResponse;
import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.entity.BorrowingRecord;
import com.example.librarymanagementsystem.entity.Patron;
import com.example.librarymanagementsystem.exception.BookNotFoundException;
import com.example.librarymanagementsystem.exception.BorrowingRecordNotFoundException;
import com.example.librarymanagementsystem.exception.PatronNotFoundException;
import com.example.librarymanagementsystem.repository.BookRepository;
import com.example.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.example.librarymanagementsystem.repository.PatronRepository;
import com.example.librarymanagementsystem.service.BorrowingRecordService;
import com.example.librarymanagementsystem.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class BorrowingRecordServiceImpl implements BorrowingRecordService {

    private final BookRepository bookRepository;

    private final PatronRepository patronRepository;
    
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final DtoMapper dtoMapper;

    @Override
    public ApiResponse<Book> borrowBook(Long bookId, String patronEmail) {
        Book book = bookRepository.findById(bookId)
               .orElseThrow(() -> new BookNotFoundException("Book not found"));
        Patron patron = patronRepository.findByEmail(patronEmail)
                .orElseThrow(() -> new PatronNotFoundException("Patron with email " + patronEmail + " not found"));

        BorrowingRecord borrowing = new BorrowingRecord();
        borrowing.setBook(book);
        borrowing.setPatron(patron);
        borrowing.setBorrowingDate(LocalDate.now());
        borrowingRecordRepository.save(borrowing);
        return new ApiResponse<>("Borrow book successful","Success", HttpStatus.OK, borrowing.getBook());
    }

    @Override
    public ApiResponse<String> returnBook(Long bookId, Long patronId) {
        BorrowingRecord borrowing = borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId)
                .orElseThrow(() -> new BorrowingRecordNotFoundException("Borrowing record not found for book " + bookId + " and patron " + patronId));

        borrowing.setReturnDate(LocalDate.now());
        borrowingRecordRepository.save(borrowing);
        return new ApiResponse<>("Returned book recorded","Success",HttpStatus.OK);
    }
}
