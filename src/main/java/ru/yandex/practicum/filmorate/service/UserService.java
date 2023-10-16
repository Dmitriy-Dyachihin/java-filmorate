package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final InMemoryUserStorage userStorage;

    public User createUser(User user) {
        validate(user, "Добавлен");
        log.info("Добавлен пользователь {}", user.getName());
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        validate(user, "Обновлен");
        log.info("Обновлен пользователь {}", user.getName());
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Integer userId) {
        return userStorage.getUserById(userId);
    }

    public void addFriend(Integer userId, Integer friendId) {
        userStorage.addFriend(userId, friendId);
        userStorage.addFriend(friendId, userId);
        log.debug("Пользователь {} добавил в друзья пользователя {}", userStorage.getUserById(userId).getName(),
                userStorage.getUserById(friendId).getName());
    }

    public void removeFriend(Integer userId, Integer friendId) {
        userStorage.getUserById(userId).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(userId);
        log.debug("Пользователь {} удалил из друзей пользователя {}", userStorage.getUserById(userId).getName(),
                userStorage.getUserById(friendId).getName());
    }

    public List<User> getFriendsOfUser(Integer userId) {
        return userStorage.getFriendsOfUser(userId);
    }

    public Set<User> getCommonFriends(Integer userId, Integer friendId) {
        return userStorage.getFriendsId(userId)
                .stream()
                .filter(userStorage.getFriendsId(friendId)::contains)
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }

    private void validate(User user, String message) {
        if (!user.getEmail().contains("@") || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Почта не может быть пустой и должна содержать @");
        } else if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и не должен содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("{} пользователь: {}, email: {}", message, user.getName(), user.getEmail());
    }
}
