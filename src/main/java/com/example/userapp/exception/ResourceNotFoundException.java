package com.example.userapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private String res;
    private long id;

    public ResourceNotFoundException(String res, long id) {
        super(String.format("%s not found for ID: %s", res, id));
        this.res = res;
        this.id = id;
    }
}
