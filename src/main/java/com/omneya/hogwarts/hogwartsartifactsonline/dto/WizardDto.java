package com.omneya.hogwarts.hogwartsartifactsonline.dto;

import jakarta.validation.constraints.NotEmpty;

public record WizardDto(
                        Long id,
                        @NotEmpty(message = "Name Is Required")
                        String name,
                        Integer numberOfArtifacts) {
}
