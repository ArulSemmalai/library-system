package com.my.library.system.repository;

import com.my.library.system.entity.Book;
import com.my.library.system.entity.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord,Long> {
    public Optional<BorrowingRecord> findByBook_BookIdAndBorrower_BorrowerId(String bookId, String borrowerId);
    public void deleteById(Long id);
    public Optional<Book> findByBook_BookId(String bookId);

}
