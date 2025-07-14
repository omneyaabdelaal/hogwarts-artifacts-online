package com.omneya.hogwarts.hogwartsartifactsonline.dto;

import jakarta.validation.constraints.NotEmpty;

public record ArtifactDto(String id,
                          @NotEmpty(message = "Name Is Required")
                          String name,
                          @NotEmpty(message = "Description Is Required")
                          String description,
                          @NotEmpty(message = "Image URL Is Required")
                          String imageUrl,
                          WizardDto owner) {

}
