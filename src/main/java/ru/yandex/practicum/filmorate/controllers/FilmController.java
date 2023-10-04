package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.FilmManager;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmManager filmManager = new FilmManager();

    @GetMapping
    public List<Film> getFilms() {
        List<Film> films = new ArrayList<>(filmManager.getFilms().values());
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        if (filmManager.getFilms().containsKey(film.getId())) {
            throw new RuntimeException("Уже есть такой фильм");
        } else {
            filmManager.validate(film, "Добавлен");
            return filmManager.addFilm(film);
        }
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!filmManager.getFilms().containsKey(film.getId())) {
            throw new RuntimeException("Нет такого фильма");
        } else {
            filmManager.validate(film, "Обновлен");
            return filmManager.updateFilm(film);
        }
    }
}