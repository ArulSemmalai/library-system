package com.my.library.system.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Borrower {
    @Id
    private String borrowerId;
    private String name;
    private String email;

/*    @OneToMany(mappedBy = "borrowedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> borrowedBooks = new ArrayList<>();*/
}
