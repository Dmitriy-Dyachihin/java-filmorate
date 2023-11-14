package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDaoImpl;

import java.util.Collection;

@Service
@AllArgsConstructor
public class MpaService {

    private final MpaDaoImpl mpaDao;

    public Collection<Mpa> getMpa() {
        return mpaDao.getMpa();
    }

    public Mpa getMpaByMpaId(int id) {
        return mpaDao.getMpaById(id);
    }
}
