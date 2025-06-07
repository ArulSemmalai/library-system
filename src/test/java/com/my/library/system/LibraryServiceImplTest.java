package com.my.library.system;

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
import com.my.library.system.service.LibraryServiceImpl;
import com.my.library.system.util.LibraryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryServiceImplTest {

    @Mock
    private BookRepository bookRepo;

    @Mock
    private BorrowerRepository borrowerRepo;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private LibraryMapper libraryMapper;

    @InjectMocks
    private LibraryServiceImpl libraryService;

    private Book book;
    private Borrower borrower;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setBookId("1234");
        book.setTitle("Effective Java");
        book.setAvailableCopies(1);

        borrower = new Borrower();
        borrower.setBorrowerId("9999");
        borrower.setName("arul kumar");
    }

    @Test
    void testBorrowBook_Success() throws BorrowerNotFoundException, BookNotFoundException,
            BookAlreadyBorrowedException, BookNotAvailableException {

        // Arrange: mock Book and Borrower data
/*        Book book = new Book();
        book.setBookId("1234");
        book.setTitle("Effective Java");
        book.setAvailableCopies(3);

        Borrower borrower = new Borrower();
        borrower.setBorrowerId("9999");
        borrower.setName("arul kumar");*/

        when(bookRepo.findById("1234")).thenReturn(Optional.of(book));
        when(bookRepo.save(book)).thenReturn(book);
        when(borrowerRepo.findById("9999")).thenReturn(Optional.of(borrower));
        when(borrowingRecordRepository.findByBook_BookIdAndBorrower_BorrowerId("1234", "9999"))
                .thenReturn(Optional.empty());

        // Act: call the method under test
        String response = libraryService.borrowBook("1234", "9999");

        // Assert: check the expected result
        assertTrue(response.contains("Effective Java"));
        assertTrue(response.contains("arul kumar"));
        assertTrue(response.contains("1234"));

        verify(borrowingRecordRepository).save(any(BorrowingRecord.class));
        verify(bookRepo).save(any(Book.class));
    }


    @Test
    void testBorrowBook_BookNotFound() {
        when(bookRepo.findById("1234")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () ->
                libraryService.borrowBook("1234", "abcd"));
    }

    @Test
    void testBorrowBook_BookNotAvailable() {
        book.setAvailableCopies(0);
        when(bookRepo.findById("1234")).thenReturn(Optional.of(book));

        assertThrows(BookNotAvailableException.class, () ->
                libraryService.borrowBook("1234", "abcd"));
    }

    @Test
    void testBorrowBook_BorrowerNotFound() {
        when(bookRepo.findById("1234")).thenReturn(Optional.of(book));
        when(borrowerRepo.findById("9999")).thenReturn(Optional.empty());

        assertThrows(BorrowerNotFoundException.class, () ->
                libraryService.borrowBook("1234", "9999"));
    }

    @Test
    void testBorrowBook_AlreadyBorrowed() {
        when(bookRepo.findById("1234")).thenReturn(Optional.of(book));
        when(borrowerRepo.findById("9999")).thenReturn(Optional.of(borrower));
        when(borrowingRecordRepository.findByBook_BookIdAndBorrower_BorrowerId("1234", "9999"))
                .thenReturn(Optional.of(new BorrowingRecord()));

        assertThrows(BookAlreadyBorrowedException.class, () ->
                libraryService.borrowBook("1234", "9999"));
    }

    @Test
    void testReturnBook_Success() throws BookNotFoundException {

        BorrowingRecord record = new BorrowingRecord();
        record.setId(1L);
        record.setBook(book);
        record.setBorrower(borrower);

        when(bookRepo.findById("1234")).thenReturn(Optional.of(book));
        when(borrowingRecordRepository.findByBook_BookIdAndBorrower_BorrowerId("1234", "9999"))
                .thenReturn(Optional.of(record));

        String result = libraryService.returnBook("1234", "9999");

        assertTrue(result.contains("Book returned successfully"));
        assertTrue(result.contains("arul kumar"));
        assertTrue(result.contains("Effective Java"));

        verify(bookRepo).save(any(Book.class));
        verify(borrowingRecordRepository).deleteById(1L);
    }
    @Test
    void testReturnBook_BookNotFound() {
        when(bookRepo.findById("9999")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            libraryService.returnBook("9999", "7777");
        });

        verify(borrowingRecordRepository, never()).deleteById(anyLong());
    }

    @Test
    void testReturnBook_NotBorrowed() throws BookNotFoundException {
        Book book = new Book();
        book.setBookId("1234");
        book.setTitle("Effective Java");
        book.setAvailableCopies(2);

        when(bookRepo.findById("1234")).thenReturn(Optional.of(book));
        when(borrowingRecordRepository.findByBook_BookIdAndBorrower_BorrowerId("1234", "9999"))
                .thenReturn(Optional.empty());

        String result = libraryService.returnBook("1234", "9999");

        assertTrue(result.contains("Book was not borrowed"));
        assertTrue(result.contains("Effective Java"));

        verify(bookRepo, never()).save(any(Book.class));
        verify(borrowingRecordRepository, never()).deleteById(any());
    }

    @Test
    void testCreateBook_NewBook_Success() {
        BookRequest request = new BookRequest("1234567890", "Effective Java", "Joshua Bloch");
        Book mappedBook = new Book();
        mappedBook.setIsbn("1234567890");
        mappedBook.setAuthor("Effective Java");
        mappedBook.setTitle("Joshua Bloch");


        when(bookRepo.findByIsbn("1234567890")).thenReturn(Collections.emptyList());
        when(libraryMapper.toBook(request)).thenReturn(mappedBook);
        when(bookRepo.existsByBookId(any())).thenReturn(false);
        when(bookRepo.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(libraryMapper.toBookResponse(any(Book.class))).thenReturn(new BookResponse("9999", "1234567890","Effective Java", "Joshua Bloch", 1));

        BookResponse response = libraryService.createBook(request);

        assertEquals("Effective Java", response.title());
        verify(bookRepo).save(any(Book.class));
    }

    @Test
    void testCreateBook_ExistingBook_IncrementCopies() {

        Book existing = new Book();
        existing.setIsbn("1234567890");
        existing.setTitle("Effective Java");
        existing.setAuthor("Joshua Bloch");

        existing.setBookId("1234");

        BookRequest request = new BookRequest("1234567890", "Effective Java", "Joshua Bloch");

        when(bookRepo.findByIsbn("1234567890")).thenReturn(List.of(existing));
        when(bookRepo.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(libraryMapper.toBookResponse(any(Book.class))).thenReturn(new BookResponse("1234","1234567890","Effective Java", "Joshua Bloch", 2));

        BookResponse response = libraryService.createBook(request);

        assertEquals(2, response.availableCopies());
        verify(bookRepo).save(existing);
    }

    @Test
    void testCreateBook_BookExistsWithDifferentAuthorOrTitle() {
        BookRequest request = new BookRequest("1234567890", "Effective C++", "Tom Bloch");

        Book existing = new Book();
        Book mapped = new Book();

        existing.setIsbn("1234567890");
        existing.setTitle("Effective SQL");
        existing.setAuthor("Joshua Kim");

        existing.setBookId("1234");

        mapped.setIsbn("1234567890");
        mapped.setAuthor("Tom Bloch");
        mapped.setTitle("Effective C++");

        when(bookRepo.findByIsbn("1234567890")).thenReturn(List.of(existing));
        when(libraryMapper.toBook(request)).thenReturn(mapped);
        when(bookRepo.existsByBookId(any())).thenReturn(false);
        when(bookRepo.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(libraryMapper.toBookResponse(any(Book.class))).thenReturn(new BookResponse("5678", "1234567890","Effective C++", "Tom Bloch", 1));

        BookResponse response = libraryService.createBook(request);

        assertEquals("Effective C++", response.title());
        verify(bookRepo).save(any(Book.class));
    }

    @Test
    void testCreateBorrower_Success() {
        BorrowerRequest request = new BorrowerRequest("arul kumar", "arul.kumar@gmail.com");

        Borrower mapped = new Borrower();
        mapped.setEmail("arul.kumar@gmail.com");
        mapped.setName("arul kumar");

        Borrower saved = new Borrower();
        saved.setName("arul kumar");
        saved.setEmail("arul.kumar@gmail.com");

        BorrowerResponse expectedResponse = new BorrowerResponse("1234", "arul kumar", "arul.kumar@gmail.com");

        when(libraryMapper.toBorrower(request)).thenReturn(mapped);
        when(bookRepo.existsByBookId(any())).thenReturn(false); // For generateUniqueId
        when(borrowerRepo.save(any(Borrower.class))).thenReturn(saved);
        when(libraryMapper.toBorrowerResponse(saved)).thenReturn(expectedResponse);

        BorrowerResponse response = libraryService.CreateBorrower(request);

        assertEquals("1234", response.borrowerId());
        assertEquals("arul kumar", response.name());
        assertEquals("arul.kumar@gmail.com", response.email());
        verify(borrowerRepo).save(any(Borrower.class));
    }

    @Test
    void testCreateBorrower_DuplicateIdRetry() {
        BorrowerRequest request = new BorrowerRequest("arul kumar", "arul.kumar@gmail.com");

        Borrower mapped = new Borrower();
        mapped.setEmail("arul.kumar@gmail.com");
        mapped.setName("arul kumar");

        Borrower saved = new Borrower();
        saved.setName("arul kumar");
        saved.setEmail("arul.kumar@gmail.com");

        // First ID already exists, second does not
        when(libraryMapper.toBorrower(request)).thenReturn(mapped);
        when(bookRepo.existsByBookId(any())).thenReturn(true, false); // Retry logic
        when(borrowerRepo.save(any(Borrower.class))).thenReturn(saved);
        when(libraryMapper.toBorrowerResponse(saved)).thenReturn(new BorrowerResponse("5678", "arul kumar", "arul.kumar@gmail.com"));

        BorrowerResponse response = libraryService.CreateBorrower(request);

        assertEquals("5678", response.borrowerId());
        verify(bookRepo, times(2)).existsByBookId(any()); // Ensure retry happened
    }

}
