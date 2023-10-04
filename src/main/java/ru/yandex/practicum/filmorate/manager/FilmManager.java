package ru.yandex.practicum.filmorate.manager;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString
@Slf4j
public class FilmManager {

    private final Map<Integer, Film> films = new HashMap<>();
    private static int id = 0;

    public int assignId() {
        return ++id;
    }

    public Film addFilm(Film film) {
        int newId = assignId();
        film.setId(newId);
        films.put(newId, film);
        return film;
    }

    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public void validate(Film film, String message) throws ValidationException {
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
