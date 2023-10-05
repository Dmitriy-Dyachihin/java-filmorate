package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final UserController userController = new UserController();

    @Test
    void shouldAddUser() {
        User user = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        userController.addUser(user);
        assertEquals(userController.getUsers(), List.of(user));
    }

    @Test
    void shouldAddUserWithEmptyName() {
        User user = User.builder()
                .id(1)
                .name("")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        userController.addUser(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void shouldNotAddUserWithEmptyEmail() {
        User user = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.addUser(user);
            }
        });
    }

    @Test
    void shouldNotAddUserWithUncorrectEmail() {
        User user = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("usermail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.addUser(user);
            }
        });
    }

    @Test
    void shouldNotAddUserWithEmptyLogin() {
        User user = User.builder()
                .id(1)
                .name("Имя 1")
                .login(null)
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.addUser(user);
            }
        });
    }

    @Test
    void shouldNotAddUserWithUncorrectLogin() {
        User user = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login 1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.addUser(user);
            }
        });
    }

    @Test
    void shouldNotAddUserWithUncorrectBirthday() {
        User user = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(2990, 1, 1))
                .build();

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.addUser(user);
            }
        });
    }

    @Test
    void shouldUpdateUser() {
        User user = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        userController.addUser(user);
        user.setLogin("New_login1");
        userController.updateUser(user);
        assertEquals("New_login1", user.getLogin());
    }

    @Test
    void shouldNotUpdateUser() {
        User user = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        userController.addUser(user);
        user.setLogin("New_login1");
        user.setId(200);
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws RuntimeException {
                userController.updateUser(user);
            }
        });
    }

    @Test
    void shouldGetUsers() {
        User user1 = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        userController.addUser(user1);
        User user2 = User.builder()
                .id(1)
                .name("Имя 1")
                .login("Login_1")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        userController.addUser(user2);
        assertEquals(userController.getUsers(), List.of(user1, user2));
    }

    @Test
    void shouldGetZeroUsers() {
        List<User> users = new ArrayList<User>(userController.getUsers());
        assertEquals(users, Collections.emptyList());
    }
}