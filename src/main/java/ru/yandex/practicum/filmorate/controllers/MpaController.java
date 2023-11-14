package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> getMpa() {
        log.info("Получен GET-запрос к эндпоинту - /mpa");
        return mpaService.GetMpa();
    }

    @GetMapping("/{id}")
    public Mpa getMpaByMpaId(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту - /mpa/{id}");
        return mpaService.getMpaByMpaId(id);
    }
}
