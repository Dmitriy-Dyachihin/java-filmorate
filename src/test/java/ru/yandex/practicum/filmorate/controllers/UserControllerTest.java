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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserController userController;

    @Test
    void shouldAddUser() throws Exception {
        User user = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        Mockito.when(userController.addUser(Mockito.any())).thenReturn(user);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void shouldGetUsers() throws Exception {
        User user = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        Mockito.when(userController.getUsers()).thenReturn(Collections.singletonList(user));
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(user))));
    }

    @Test
    void shouldGetUserById() throws Exception {
        User user = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        Mockito.when(userController.getUserById(user.getId())).thenReturn(user);
        mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));

    }

    @Test
    void shouldUpdateUser() throws Exception {
        User user = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        user.setName("Новое имя");
        Mockito.when(userController.updateUser(user)).thenReturn(user);
        mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Новое имя"))
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void shouldAddAndDeleteFriend() throws Exception {
        User user1 = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User user2 = User.builder()
                .id(1)
                .name("Имя 2")
                .login("Login_2")
                .email("userr@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        mockMvc.perform(put("/users/{id}/friends/{friendId}", user1.getId(), user2.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/users/{id}/friends/{friendId}", user1.getId(), user2.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCommonFriend() throws Exception {
        User user1 = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User user2 = User.builder()
                .id(2)
                .name("Имя 2")
                .login("Login_2")
                .email("userr@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User user3 = User.builder()
                .id(3)
                .name("Имя 3")
                .login("Login_3")
                .email("usserr@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        mockMvc.perform(put("/users/{id}/friends/{friendId}", user1.getId(), user2.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(put("/users/{id}/friends/{friendId}", user3.getId(), user2.getId()))
                .andExpect(status().isOk());
        Mockito.when(userController.getCommonFriends(user1.getId(), user3.getId()))
                .thenReturn(Collections.singletonList(user2));
        mockMvc.perform(get("/users/{id}/friends/common/{friendId}", user1.getId(), user3.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(user2))));
    }

}