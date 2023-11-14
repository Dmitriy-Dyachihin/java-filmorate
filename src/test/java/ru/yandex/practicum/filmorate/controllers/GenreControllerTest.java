package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private GenreController genreController;

    @Test
    void shouldGetGenres() throws Exception {
        Genre genre = new Genre();
        genre.setName("Комедия");
        Mockito.when(genreController.getGenres()).thenReturn(Collections.singletonList(genre));
        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(genre))));
    }

    @Test
    void shouldGetGenreById() throws Exception {
        Genre genre = new Genre();
        genre.setName("Комедия");
        Mockito.when(genreController.getGenreByGenreId(genre.getId())).thenReturn(genre);
        mockMvc.perform(get("/genres/{id}", genre.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(genre)));
    }
}