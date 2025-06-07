package com.my.library.system.dto;

import jakarta.validation.constraints.NotBlank;

public record BookRequest(
        @NotBlank(message = "ISBN must not be blank")
        String isbn,
        @NotBlank(message = "Title must not be blank")
        String title,
        @NotBlank(message = "Author must not be blank")
        String author) {}

