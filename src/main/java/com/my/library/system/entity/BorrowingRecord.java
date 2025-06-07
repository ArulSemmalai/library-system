package com.my.library.system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BorrowingRecord {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowerId")
    private Borrower borrower;
}
