package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDaoImpl;

import java.util.Collection;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreDaoImpl genreDao;

    public Collection<Genre> getGenres() {
        return genreDao.getGenres();
    }

    public Genre getGenreByGenreId(int genreId) {
        return genreDao.getGenreByGenreId(genreId);
    }
}
