package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    User getUserById(Integer id);

    List<User> getFriendsOfUser(Integer userId);

    void addFriend(Integer userId, Integer friendId);

    Set<Integer> getFriendsId(Integer id);
}
