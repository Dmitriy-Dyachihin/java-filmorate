package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FilmDontExistException extends RuntimeException {

    public FilmDontExistException() {
    }

    public FilmDontExistException(String message) {
        super(message);
    }

}
