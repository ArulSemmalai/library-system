package com.my.library.system.util;

import com.my.library.system.dto.BookRequest;
import com.my.library.system.dto.BookResponse;
import com.my.library.system.dto.BorrowerRequest;
import com.my.library.system.dto.BorrowerResponse;
import com.my.library.system.entity.Book;
import com.my.library.system.entity.Borrower;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LibraryMapper {
    BookResponse toBookResponse(Book book);
    BorrowerResponse toBorrowerResponse(Borrower borrower);
    Book toBook(BookRequest request);
    Borrower toBorrower(BorrowerRequest borrowerRequest);
}

