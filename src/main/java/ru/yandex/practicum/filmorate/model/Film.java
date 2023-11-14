package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.anotations.CorrectDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    @PositiveOrZero(message = "Id не может быть отрицательным")
    private int id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Max(value = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    @CorrectDate(message = "Дата релиза — не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    @NotNull
    private Mpa mpa;

    private List<Genre> genres = new ArrayList<>();

}
