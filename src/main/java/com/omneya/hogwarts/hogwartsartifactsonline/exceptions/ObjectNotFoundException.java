package com.omneya.hogwarts.hogwartsartifactsonline.exceptions;

public  class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(Long id) {
        super("Could Not Find Wizard with Id "+id+" :(!");
    }

    public ObjectNotFoundException(String id) {
        super("Could Not Find Artifact with Id "+id+" :(!");
    }
}
