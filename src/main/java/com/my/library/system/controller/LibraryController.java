package com.my.library.system.controller;

import com.my.library.system.dto.BookRequest;
import com.my.library.system.dto.BookResponse;
import com.my.library.system.dto.BorrowerRequest;
import com.my.library.system.dto.BorrowerResponse;
import com.my.library.system.exception.BookAlreadyBorrowedException;
import com.my.library.system.exception.BookNotAvailableException;
import com.my.library.system.exception.BookNotFoundException;
import com.my.library.system.exception.BorrowerNotFoundException;
import com.my.library.system.service.LibraryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class LibraryController {

    @Autowired
    private LibraryService service;

    @PostMapping("/books/add")
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
        return ResponseEntity.ok(service.createBook(bookRequest));
    }

    @PostMapping("/borrowers/add")
    public ResponseEntity<BorrowerResponse> createBorrower(@Valid @RequestBody BorrowerRequest borrowerRequest) {
        return ResponseEntity.ok(service.CreateBorrower(borrowerRequest));
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookResponse>> getBooks() {
        return ResponseEntity.ok(service.listBooks());
    }

    @GetMapping("/borrowers")
    public ResponseEntity<List<BorrowerResponse>> getBorrowers() {
        return ResponseEntity.ok(service.listBorrower());
    }

    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(
            @RequestParam("bookId") String bookId,
            @RequestParam("borrowerId") String borrowerId) throws BorrowerNotFoundException, BookNotFoundException, BookAlreadyBorrowedException, BookNotAvailableException {
        return ResponseEntity.ok(service.borrowBook(bookId, borrowerId));
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam("bookId") String bookId,
                                             @RequestParam("borrowerId") String borrowerId ) throws BookNotFoundException {
        return ResponseEntity.ok(service.returnBook(bookId,borrowerId));
    }
    
}
