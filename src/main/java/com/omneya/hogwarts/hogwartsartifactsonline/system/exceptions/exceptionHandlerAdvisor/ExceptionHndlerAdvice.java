package com.omneya.hogwarts.hogwartsartifactsonline.system.exceptions.exceptionHandlerAdvisor;

import com.omneya.hogwarts.hogwartsartifactsonline.system.Result;
import com.omneya.hogwarts.hogwartsartifactsonline.system.StatusCode;
import com.omneya.hogwarts.hogwartsartifactsonline.system.exceptions.ArtifactNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHndlerAdvice {

    @ExceptionHandler(value = { ArtifactNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleArtifactNotFoundException(ArtifactNotFoundException e) {
        return new Result(false, StatusCode.NOT_FOUND,"Could Not Find Artifact with Id 1250808601744904191 :(!");
    }

    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String,String> errors = new HashMap<>();

        fieldErrors.forEach(error->{
            errors.put(error.getField(),error.getDefaultMessage());
        });
        return new Result(false,StatusCode.INVALID_ARGUMENT,"Provided Argument Not Valid",errors);
    }
}
