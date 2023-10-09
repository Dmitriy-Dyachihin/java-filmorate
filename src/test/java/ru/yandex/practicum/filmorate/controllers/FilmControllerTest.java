package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    private final FilmController filmController = new FilmController();

    @Test
    void shouldAddFilm() {
        Film film = Film.builder()
                .id(1)
                .name("Название фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        filmController.addFilm(film);
        assertEquals(filmController.getFilms(), List.of(film));
    }

    @Test
    void shouldNotAddFilmWithEmptyName() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.addFilm(film);
            }
        });
    }

    @Test
    void shouldNotAddFilmWithWhiteSpaceInName() {
        Film film = Film.builder()
                .id(1)
                .name(" ")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.addFilm(film);
            }
        });
    }

    @Test
    void shouldNotAddFilmWithUncorrectDescription() {
        Film film = Film.builder()
                .id(1)
                .name("Название фильма 1")
                .description("Оооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооо" +
                        "ооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооо" +
                        "ооооооооооооооооочень длиное описание фильма 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.addFilm(film);
            }
        });
    }

    @Test
    void shouldNotAddFilmWithUncorrectReleaseDate() {
        Film film = Film.builder()
                .id(1)
                .name("Название фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(1700, 1, 1))
                .duration(100)
                .build();

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.addFilm(film);
            }
        });
    }

    @Test
    void shouldNotAddFilmWithUncorrectDuration() {
        Film film = Film.builder()
                .id(1)
                .name("Название фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(-100)
                .build();

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.addFilm(film);
            }
        });
    }

    @Test
    void shouldUpdateFilm() {
        Film film = Film.builder()
                .id(1)
                .name("Название фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        filmController.addFilm(film);
        film.setName("Новое название фильма 1");
        filmController.updateFilm(film);
        assertEquals("Новое название фильма 1", film.getName());
    }

    @Test
    void shouldNotUpdateFilm() {
        Film film = Film.builder()
                .id(1)
                .name("Название фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        filmController.addFilm(film);
        film.setName("Новое название фильма 1");
        film.setId(200);
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws RuntimeException {
                filmController.updateFilm(film);
            }
        });
    }

    @Test
    void shouldGetFilms() {
        Film film1 = Film.builder()
                .id(1)
                .name("Название фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        filmController.addFilm(film1);
        Film film2 = Film.builder()
                .id(2)
                .name("Название фильма 2")
                .description("Описание фильма 2")
                .releaseDate(LocalDate.of(2000, 1, 2))
                .duration(100)
                .build();

        filmController.addFilm(film2);
        assertEquals(filmController.getFilms(), List.of(film1, film2));
    }

    @Test
    void shouldGetZeroFilms() {
        List<Film> films = new ArrayList<Film>(filmController.getFilms());
        assertEquals(films, Collections.emptyList());
    }
}