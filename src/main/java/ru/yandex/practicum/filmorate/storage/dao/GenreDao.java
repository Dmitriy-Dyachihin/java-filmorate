package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreDao {

    Collection<Genre> getGenres();

    Collection<Genre> getGenreByFilmId(int filmId);

    Genre getGenreByGenreId(int genreId);

    void addGenreToFilm(int filmId, Collection<Genre> genres);

    void removeFilmGenres(int filmId);
}
