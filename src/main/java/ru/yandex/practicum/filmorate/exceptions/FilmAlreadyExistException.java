package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FOUND)
public class FilmAlreadyExistException extends RuntimeException {

    public FilmAlreadyExistException() {
    }

    public FilmAlreadyExistException(String message) {
        super(message);
    }
}
