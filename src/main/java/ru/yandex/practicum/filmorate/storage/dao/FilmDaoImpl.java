package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.FilmDontExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

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
public class FilmDaoImpl implements FilmDao{

    private final JdbcTemplate jdbcTemplate;
    private final GenreDaoImpl genreDao;
    private final UserDaoImpl userDao;

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO film (name, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        if (!film.getGenres().isEmpty()) {
            genreDao.addGenreToFilm(film.getId(), film.getGenres());
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, " +
                "mpa_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        genreDao.removeFilmGenres(film.getId());
        if(!film.getGenres().isEmpty()){
            genreDao.addGenreToFilm(film.getId(), film.getGenres());
        }
        return getFilmById(film.getId());
    }

    @Override
    public Collection<Film> getFilms() {
        String sql = "SELECT * FROM film AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs, rowNum));
    }

    @Override
    public Film getFilmById(Integer id) {
        String sql = "SELECT * FROM film AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id WHERE film_id = ?";
        Film film;
        try{
            film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs, rowNum), id);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmDontExistException("Не существует фильма с указанным id");
        }
        log.info("Найден фильм с id = {}", id);
        return film;
    }

    @Override
    public Collection<Film> getPopularFilms(Integer count) {
        String sql = "SELECT * FROM film AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id LEFT OUTER JOIN likes AS l " +
                "ON f.film_id = l.film_id GROUP BY f.film_id ORDER BY COUNT(l.user_id) DESC LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs, rowNum), count);
    }

    @Override
    public void removeFilm(Film film) {
        String sql = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Integer duration = rs.getInt("duration");
        Integer mpaId = rs.getInt("mpa.mpa_id");
        String mpaName = rs.getString("mpa.name");
        Mpa mpa = new Mpa(mpaId, mpaName);
        List<Genre> genres = (List<Genre>) genreDao.getGenreByFilmId(id);
        return new Film(id, name, description, releaseDate, duration, mpa, genres);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        String sqlLike = "SELECT * FROM likes WHERE film_id = ? AND user_id = ?";
        SqlRowSet haveLiked = jdbcTemplate.queryForRowSet(sqlLike, filmId, userId);
        if(!haveLiked.next()) {
            jdbcTemplate.update(sql, filmId, userId);
        }
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        userDao.getUserById(userId);
        getFilmById(filmId);
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }
}
