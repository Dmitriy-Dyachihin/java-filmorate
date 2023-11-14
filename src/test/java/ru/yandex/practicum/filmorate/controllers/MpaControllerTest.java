package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MpaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MpaController mpaController;


    @Test
    void shouldGetMpa() throws Exception {
        Mpa mpa = new Mpa();
        mpa.setName("G");
        Mockito.when(mpaController.getMpa()).thenReturn(Collections.singletonList(mpa));
        mockMvc.perform(get("/mpa"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(mpa))));
    }

    @Test
    void shouldGetAllMpa() throws Exception {
        Mpa mpa1 = new Mpa();
        mpa1.setName("G");
        Mpa mpa2 = new Mpa();
        mpa2.setName("NC-17");
        Mockito.when(mpaController.getMpa()).thenReturn(List.of(mpa1, mpa2));
        mockMvc.perform(get("/mpa"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(mpa1, mpa2))));
    }

    @Test
    void shouldGetMpaById() throws Exception {
        Mpa mpa = new Mpa();
        mpa.setName("G");
        Mockito.when(mpaController.getMpaByMpaId(mpa.getId())).thenReturn(mpa);
        mockMvc.perform(get("/mpa/{id}", mpa.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json((objectMapper.writeValueAsString(mpa))));
    }
}