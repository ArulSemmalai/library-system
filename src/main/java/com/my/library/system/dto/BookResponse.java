package com.my.library.system.dto;

public record BookResponse(String bookId, String isbn, String title, String author, int availableCopies) {}
