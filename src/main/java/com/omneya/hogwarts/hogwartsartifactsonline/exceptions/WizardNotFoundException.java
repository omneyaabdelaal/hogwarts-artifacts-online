package com.omneya.hogwarts.hogwartsartifactsonline.exceptions;

public class WizardNotFoundException extends RuntimeException {
    public WizardNotFoundException(Long id) {
        super("Wizard with id "+id+ " not found :(");
    }
}
