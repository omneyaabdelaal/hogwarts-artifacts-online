package com.omneya.hogwarts.hogwartsartifactsonline.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(
        Long id,
        @NotEmpty(message = "User Name Is Required")
        String username,
        boolean enabled ,
        @NotEmpty(message = "Roles Are Required")
        String roles) {
}
