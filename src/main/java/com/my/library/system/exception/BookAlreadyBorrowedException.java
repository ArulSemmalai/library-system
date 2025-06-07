package com.my.library.system.exception;

public class BookAlreadyBorrowedException extends Exception{
    public BookAlreadyBorrowedException() {
    }

    public BookAlreadyBorrowedException(String message) {
        super(message);
    }

    public BookAlreadyBorrowedException(String message, Throwable cause) {
        super(message, cause);
    }
}
