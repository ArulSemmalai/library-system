package com.my.library.system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class Book {

    @Id
    private String bookId;
    private String isbn;
    private String title;
    private String author;
    private int availableCopies = 1;

}
