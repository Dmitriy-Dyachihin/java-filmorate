package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.UserDontExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
@AllArgsConstructor
public class UserDaoImpl implements UserDao {

    JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO users (email, login, name, birthdate) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return getUserById(user.getId());
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthdate = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getUserById(user.getId());
    }

    @Override
    public Collection<User> getUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs, rowNum));
    }

    @Override
    public User getUserById(Integer id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs, rowNum), id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserDontExistException("Пользователь с указанным id не существует");
        }
        return user;
    }

    @Override
    public void removeUser(User user) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getId());
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String haveFriendAlreadySql = "SELECT * FROM friends " +
                "WHERE user_id = ? AND friend_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(haveFriendAlreadySql, friendId, userId);
        boolean haveFriendAlready = sqlRowSet.next();
        String sqlSetFriend = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlSetFriend, userId, friendId, haveFriendAlready);
        if (haveFriendAlready) {
            String sqlSetStatus = "UPDATE friends SET status = true WHERE user_id = ? AND friend_id = ?";
            jdbcTemplate.update(sqlSetStatus, friendId, userId);
        }
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        String sqlDelete = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlDelete, userId, friendId);
        String sqlUpdate = "UPDATE friends SET status = false WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlUpdate, userId, friendId);
    }

    @Override
    public List<User> getFriendsOfUser(Integer userId) {
        String sql = "SELECT * FROM users, friends WHERE users.user_id = friends.friend_id AND friends.user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs, rowNum), userId);
    }

    @Override
    public Collection<User> getCommonFriends(Integer userId, Integer friendId) {
        String sql = "SELECT u.user_id, u.email, u.login, u.name, u.birthdate FROM friends AS f " +
                "INNER JOIN friends AS fr ON f.friend_id = fr.friend_id LEFT OUTER JOIN users AS u " +
                "ON f.friend_id = u.user_id WHERE f.user_id = ? AND fr.user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs, rowNum), userId, friendId);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthdate = rs.getDate("birthdate").toLocalDate();
        return new User(id, email, login, name, birthdate);
    }

}
