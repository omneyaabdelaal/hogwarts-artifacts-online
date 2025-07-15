package com.omneya.hogwarts.hogwartsartifactsonline.system.exceptionHandlerAdvisor;

import com.omneya.hogwarts.hogwartsartifactsonline.exceptions.ObjectNotFoundException;
import com.omneya.hogwarts.hogwartsartifactsonline.system.Result;
import com.omneya.hogwarts.hogwartsartifactsonline.system.StatusCode;
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

    @ExceptionHandler(value = { ObjectNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleObjectNotFoundException(ObjectNotFoundException e) {
        return new Result(false, StatusCode.NOT_FOUND, e.getMessage());
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
