package com.ing.exception;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExclusiveMovieNotExistsException extends RuntimeException {
    String db;
}
