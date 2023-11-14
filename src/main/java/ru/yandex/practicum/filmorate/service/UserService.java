package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDaoImpl;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserDaoImpl userStorage;

    public User createUser(User user) {
        validate(user);
        log.info("Добавлен пользователь {}", user.getName());
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        log.info("Обновлен пользователь {}", user.getName());
        return userStorage.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Integer userId) {
        return userStorage.getUserById(userId);
    }

    public void addFriend(Integer userId, Integer friendId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);
        userStorage.addFriend(userId, friendId);
        log.debug("Пользователь {} добавил в друзья пользователя {}", userStorage.getUserById(userId).getName(),
                userStorage.getUserById(friendId).getName());
    }

    public void removeFriend(Integer userId, Integer friendId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);
        userStorage.removeFriend(userId, friendId);
        log.debug("Пользователь {} удалил из друзей пользователя {}", userStorage.getUserById(userId).getName(),
                userStorage.getUserById(friendId).getName());
    }

    public Collection<User> getFriendsOfUser(Integer userId) {
        return userStorage.getFriendsOfUser(userId);
    }

    public Collection<User> getCommonFriends(Integer userId, Integer friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

    private void validate(User user) {
        if (!user.getEmail().contains("@") || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Почта не может быть пустой и должна содержать @");
        } else if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и не должен содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.info("Не указано имя пользователя, в качестве имени используется логин");
        }
    }
}
