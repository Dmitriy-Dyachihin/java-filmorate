package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
@Slf4j
@AllArgsConstructor
public class MpaDaoImpl implements MpaDao {

    JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpa> getMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs, rowNum));
    }

    @Override
    public Mpa getMpaById(int mpaId) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        Mpa mpa;
        try {
            mpa = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeMpa(rs, rowNum), mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Нет рейтинга с id = " + mpaId);
        }
        return mpa;

    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("mpa_id");
        String name = rs.getString("name");
        return new Mpa(id, name);
    }

}
