package com.omneya.hogwarts.hogwartsartifactsonline.system.exceptions;

public class ArtifactNotFoundException extends RuntimeException {

    public ArtifactNotFoundException(String id) {
        super("Artifact with id "+id+ " not found :(");
    }
}
