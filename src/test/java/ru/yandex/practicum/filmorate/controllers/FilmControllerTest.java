package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FilmController filmController;

    @Test
    void shouldAddFilm() throws Exception {
        Film film = Film.builder()
                .name("Название фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();
        Mockito.when(filmController.addFilm(Mockito.any())).thenReturn(film);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(film)));
    }

    @Test
    void shouldGetFilm() throws Exception {
        Film film = Film.builder()
                .name("Название фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();
        Mockito.when(filmController.getFilms()).thenReturn(Collections.singletonList(film));
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(film))));
    }

    @Test
    void shouldGetFilmById() throws Exception {
        Film film = Film.builder()
                .name("Название фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();
        Mockito.when(filmController.getFilmById(film.getId())).thenReturn(film);
        mockMvc.perform(get("/films/{id}", film.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(film)));

    }

    @Test
    void shouldUpdateUser() throws Exception {
        Film film = Film.builder()
                .name("Название фильма 1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();
        film.setName("Новое название фильма 1");
        Mockito.when(filmController.updateFilm(film)).thenReturn(film);
        mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Новое название фильма 1"))
                .andExpect(content().json(objectMapper.writeValueAsString(film)));
    }
}