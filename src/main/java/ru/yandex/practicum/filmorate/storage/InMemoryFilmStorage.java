package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.FilmDontExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private static int id = 0;

    public int assignId() {
        return ++id;
    }

    @Override
    public Film addFilm(Film film) {
        if (films.containsKey(film.getId())) {
            throw new FilmAlreadyExistException("Такой фильм уже существует");
        }
        int newId = assignId();
        film.setId(newId);
        films.put(newId, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmDontExistException("Такого фильма не существует");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer filmId) {
        if (!films.containsKey(id)) {
            throw new FilmDontExistException("Такого фильма не существует");
        }
        return films.get(id);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return getFilms().stream()
                .filter(film -> film.getLikes() != null)
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        getFilmById(filmId).getLikes().add(userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        getFilmById(filmId).getLikes().remove(userId);
    }

}
