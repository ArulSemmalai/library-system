package com.my.library.system.exception;

public class BookNotAvailableException extends Throwable{

    public BookNotAvailableException() {
    }

    public BookNotAvailableException(String message) {
        super(message);
    }

    public BookNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
