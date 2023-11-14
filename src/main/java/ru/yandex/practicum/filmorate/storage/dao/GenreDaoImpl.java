package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Repository
@AllArgsConstructor
public class GenreDaoImpl implements GenreDao {

    JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getGenres() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs, rowNum));
    }

    @Override
    public Collection<Genre> getGenreByFilmId(int filmId) {
        String sql = "SELECT * FROM genre AS g INNER JOIN film_genre AS fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs, rowNum), filmId);
    }

    @Override
    public Genre getGenreByGenreId(int genreId) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeGenre(rs, rowNum), genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Не существует жанра с указанным id = " + genreId);
        }
        return genre;
    }

    @Override
    public void addGenreToFilm(int filmId, Collection<Genre> genres) {
        for (Genre genre : genres) {
            String check = "SELECT * FROM film_genre WHERE film_id = ? AND genre_id = ?";
            if (!(jdbcTemplate.queryForRowSet(check, filmId, genre.getId()).next())) {
                String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
                jdbcTemplate.update(sql, filmId, genre.getId());
            }
        }
    }

    @Override
    public void removeFilmGenres(int filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("genre_id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }
}
