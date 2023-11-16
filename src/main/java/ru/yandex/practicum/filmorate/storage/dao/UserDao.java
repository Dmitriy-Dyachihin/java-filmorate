package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserDao {

    User addUser(User user);

    User updateUser(User user);

    Collection<User> getUsers();

    User getUserById(Integer userId);

    void removeUser(User user);

    Collection<User> getFriendsOfUser(Integer userId);

    Collection<User> getCommonFriends(Integer userId, Integer friendId);

    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    //Set<Integer> getFriendsId(Integer id);

}
