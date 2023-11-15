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

    private final UserDaoImpl userDao;

    public User createUser(User user) {
        validate(user);
        log.info("Добавлен пользователь {}", user.getName());
        return userDao.addUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        log.info("Обновлен пользователь {}", user.getName());
        return userDao.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        return userDao.getUsers();
    }

    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    public void addFriend(Integer userId, Integer friendId) {
        userDao.getUserById(userId);
        userDao.getUserById(friendId);
        userDao.addFriend(userId, friendId);
        log.debug("Пользователь {} добавил в друзья пользователя {}", userDao.getUserById(userId).getName(),
                userDao.getUserById(friendId).getName());
    }

    public void removeFriend(Integer userId, Integer friendId) {
        userDao.getUserById(userId);
        userDao.getUserById(friendId);
        userDao.removeFriend(userId, friendId);
        log.debug("Пользователь {} удалил из друзей пользователя {}", userDao.getUserById(userId).getName(),
                userDao.getUserById(friendId).getName());
    }

    public Collection<User> getFriendsOfUser(Integer userId) {
        return userDao.getFriendsOfUser(userId);
    }

    public Collection<User> getCommonFriends(Integer userId, Integer friendId) {
        return userDao.getCommonFriends(userId, friendId);
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
