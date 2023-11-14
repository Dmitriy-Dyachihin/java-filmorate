package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmDao {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getFilms();

    Film getFilmById(Integer filmId);

    Collection<Film> getPopularFilms(Integer count);

    void removeFilm(Film film);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);
}

