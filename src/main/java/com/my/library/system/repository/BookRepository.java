package com.my.library.system.repository;

import com.my.library.system.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {
    boolean existsByBookId(String code);
    List<Book> findByIsbn(String isbn);
}
