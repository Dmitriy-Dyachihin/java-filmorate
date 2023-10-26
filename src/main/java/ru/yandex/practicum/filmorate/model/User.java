package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.anotations.CorrectLogin;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    @PositiveOrZero(message = "Id не может быть отрицательным")
    private int id;

    @NotNull(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта должна содержать символ @")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @CorrectLogin(message = "Логин должен содержать символ @")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    private Set<Integer> friends;

    public User(int id, String email, String login, String name, LocalDate birthday, Set<Integer> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = friends;

        if (friends == null) {
            this.friends = new HashSet<>();
        }
    }

}
