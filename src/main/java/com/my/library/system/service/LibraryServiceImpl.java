package com.my.library.system.service;

import com.my.library.system.dto.BookRequest;
import com.my.library.system.dto.BookResponse;
import com.my.library.system.dto.BorrowerRequest;
import com.my.library.system.dto.BorrowerResponse;
import com.my.library.system.entity.Book;
import com.my.library.system.entity.Borrower;
import com.my.library.system.entity.BorrowingRecord;
import com.my.library.system.exception.BookAlreadyBorrowedException;
import com.my.library.system.exception.BookNotAvailableException;
import com.my.library.system.exception.BookNotFoundException;
import com.my.library.system.exception.BorrowerNotFoundException;
import com.my.library.system.repository.BookRepository;
import com.my.library.system.repository.BorrowerRepository;
import com.my.library.system.repository.BorrowingRecordRepository;
import com.my.library.system.util.LibraryMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LibraryServiceImpl implements LibraryService {

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private BorrowerRepository borrowerRepo;

    @Autowired
    private LibraryMapper libraryMapper;



    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Override
    public BookResponse createBook(BookRequest req) {

        List<Book> existingBook = bookRepo.findByIsbn(req.isbn());

        Optional<Book> matchingBook = existingBook.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(req.author())
                        && book.getTitle().equalsIgnoreCase(req.title()))
                .findFirst();

        if (matchingBook.isPresent()) {
            Book book = matchingBook.get();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            return libraryMapper.toBookResponse(bookRepo.save(book));
        }
            Book book = libraryMapper.toBook(req);
            book.setBookId(generateUniqueId());
            return libraryMapper.toBookResponse(bookRepo.save(book));
    }

    private String generateUniqueId() {
        String code;
        do {
            code = String.format("%04d", new Random().nextInt(10000));
        } while (bookRepo.existsByBookId(code));
        return code;
    }

    @Override
    public List<BookResponse> listBooks() {
        return bookRepo.findAll().stream()
                .map(libraryMapper::toBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowerResponse> listBorrower() {
        return borrowerRepo.findAll().stream()
                .map(libraryMapper::toBorrowerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BorrowerResponse CreateBorrower(BorrowerRequest borrowerRequest) {
        Borrower borrower = libraryMapper.toBorrower(borrowerRequest);
        borrower.setBorrowerId(generateUniqueId());
        return libraryMapper.toBorrowerResponse(borrowerRepo.save(borrower));
    }

    @Override
    @Transactional
    public String borrowBook(String bookId, String borrowerId)
            throws BookNotFoundException, BookNotAvailableException,
            BookAlreadyBorrowedException, BorrowerNotFoundException {

        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("No copies available to borrow");
        }

        Borrower borrower = borrowerRepo.findById(borrowerId)
                .orElseThrow(() -> new BorrowerNotFoundException("Borrower not found"));

        if (borrowingRecordRepository.findByBook_BookIdAndBorrower_BorrowerId(bookId, borrowerId).isPresent()) {
            throw new BookAlreadyBorrowedException("Book is already borrowed by this borrower.");
        }

        // Create and save borrowing record
        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setBorrower(borrower);
        borrowingRecordRepository.save(record);

        // Update book availability
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        Book updatedBook = bookRepo.save(book);

        return String.format("""
        {
          "bookId": "%s",
          "title": "%s",
          "borrowedBy": {
            "userId": "%s",
            "name": "%s"
          }
        }
        """,
                updatedBook.getBookId(),
                updatedBook.getTitle(),
                borrower.getBorrowerId(),
                borrower.getName()
        );
    }

    @Override
    public String returnBook(String bookId, String borrowerId) throws BookNotFoundException {

        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        Optional<BorrowingRecord> recordOpt = borrowingRecordRepository.findByBook_BookIdAndBorrower_BorrowerId(bookId, borrowerId);
        String response = null;

        if (recordOpt.isPresent()){

            Borrower borrower = recordOpt.get().getBorrower();

            recordOpt.get().getBook().setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepo.save(book);
            borrowingRecordRepository.deleteById(recordOpt.get().getId());

            response = String.format("""
                            {
                              "message": "Book returned successfully",
                              "book": {
                                "bookId": "%s",
                                "title": "%s"
                              },
                              "returnedBy": {
                                "userId": "%s",
                                "name": "%s"
                              }
                            }
                            """,
                    book.getBookId(),
                    book.getTitle(),
                    borrower.getBorrowerId(),
                    borrower.getName()
            );

        }else{
            response = String.format("""
            {
              "message": "Book was not borrowed",
              "book": {
                "bookId": "%s",
                "title": "%s"
              }
            }
            """,
                    book.getBookId(),
                    book.getTitle()
            );
        }
        return response;
    }

}
