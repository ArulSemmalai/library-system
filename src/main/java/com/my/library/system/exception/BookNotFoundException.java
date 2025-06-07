package com.my.library.system.exception;


public class BookNotFoundException extends Exception{

    public BookNotFoundException() {
    }

    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
