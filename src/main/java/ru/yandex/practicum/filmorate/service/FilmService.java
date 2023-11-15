package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.FilmDaoImpl;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmDaoImpl filmDao;

    public Film createFilm(Film film) {
        validate(film, "Добавлен");
        log.info("Добавлен фильм {}", film.getName());
        return filmDao.addFilm(film);
    }

    public Film updateFilm(Film film) {
        validate(film, "Обновлен");
        log.info("Обновлен фильм {}", film.getName());
        return filmDao.updateFilm(film);
    }

    public Collection<Film> getAllFilms() {
        return filmDao.getFilms();
    }

    public Film getFilmById(Integer id) {
        return filmDao.getFilmById(id);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return filmDao.getPopularFilms(count);
    }

    public void addLike(Integer filmId, Integer userId) {
        filmDao.addLike(filmId, userId);
        log.info("Добавлен лайк фильму {}", filmDao.getFilmById(filmId).getName());
    }

    public void removeLIke(Integer filmId, Integer userId) {
        filmDao.removeLike(filmId, userId);
        log.info("Удален лайк у фильма {}", filmDao.getFilmById(filmId).getName());
    }

    private void validate(Film film, String message) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не больше 200 символов");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность не может быть отрицательной");
        }
        log.debug("{} фильм {}", message, film.getName());
    }
}
