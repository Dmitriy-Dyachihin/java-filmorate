package ru.yandex.practicum.filmorate.anotations;

import ru.yandex.practicum.filmorate.validators.LoginValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoginValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectLogin {
    String message() default "{CorrectLogin}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
