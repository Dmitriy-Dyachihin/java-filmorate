package ru.yandex.practicum.filmorate.manager;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString
@Slf4j
public class UserManager {

    private final Map<Integer, User> users = new HashMap<>();
    private static int id = 0;

    public int assignId() {
        return ++id;
    }

    public User addUser(User user) {
        int newId = assignId();
        user.setId(newId);
        users.put(newId, user);
        return user;
    }

    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public void validate(User user, String message) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("{} пользователь: {}, email: {}", message, user.getName(), user.getEmail());
    }
}
