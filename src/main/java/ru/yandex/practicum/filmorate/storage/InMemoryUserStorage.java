package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserDontExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private static int id = 0;

    public int assignId() {
        return ++id;
    }

    @Override
    public User addUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistException("Пользователь с заданным id уже существует");
        }
        int newId = assignId();
        user.setId(newId);
        users.put(newId, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserDontExistException("Пользователь с заданным id не существует");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserDontExistException("Пользователь с заданным id не существует");
        }
        return users.get(id);
    }

    @Override
    public List<User>   getFriendsOfUser(Integer userId) {
        return users.get(userId).getFriends()
                .stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (!users.containsKey(userId) || !users.containsKey(friendId)) {
            throw new UserDontExistException("Пользователь с заданным id не существует");
        }
        getUserById(userId).getFriends().add(friendId);
    }

    public Set<Integer> getFriendsId(Integer id) {
        return getUserById(id).getFriends();
    }

}
