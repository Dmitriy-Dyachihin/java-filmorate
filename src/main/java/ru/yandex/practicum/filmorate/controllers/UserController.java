package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.UserManager;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserManager userManager = new UserManager();

    @GetMapping
    public List<User> getUsers() {
        List<User> users = new ArrayList<>(userManager.getUsers().values());
        log.debug("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @PostMapping
        public User addUser(@Valid @RequestBody User user) {
        if (userManager.getUsers().containsKey(user.getId())) {
            throw new RuntimeException("Уже есть такой пользователь");
        }
        userManager.validate(user, "Добавлен");
        return userManager.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!userManager.getUsers().containsKey(user.getId())) {
            throw new RuntimeException("Нет такого пользователя");
        }
        userManager.validate(user, "Обновлен");
        return userManager.updateUser(user);
    }
}
