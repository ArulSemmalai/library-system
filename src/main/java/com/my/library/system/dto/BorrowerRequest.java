package com.my.library.system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record BorrowerRequest(
        @NotBlank(message = "Name must not be blank")
        String name,
        @Email(message = "Invalid email")
        String email){}
