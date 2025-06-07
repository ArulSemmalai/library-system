package com.my.library.system.service;

import com.my.library.system.dto.BookRequest;
import com.my.library.system.dto.BookResponse;
import com.my.library.system.dto.BorrowerRequest;
import com.my.library.system.dto.BorrowerResponse;
import com.my.library.system.exception.BookAlreadyBorrowedException;
import com.my.library.system.exception.BookNotAvailableException;
import com.my.library.system.exception.BookNotFoundException;
import com.my.library.system.exception.BorrowerNotFoundException;
import com.my.library.system.repository.BorrowingRecordRepository;

import java.util.List;

public interface LibraryService {

    public BookResponse createBook(BookRequest bookRequest);
    public BorrowerResponse CreateBorrower(BorrowerRequest borrowerRequest);
    public List<BookResponse> listBooks();
    public String borrowBook(String bookId, String borrowerId) throws BookNotFoundException, BookNotAvailableException,
    BookAlreadyBorrowedException, BorrowerNotFoundException;
    public String returnBook(String bookId, String borrowerId)throws BookNotFoundException;
    public List<BorrowerResponse> listBorrower();
}
