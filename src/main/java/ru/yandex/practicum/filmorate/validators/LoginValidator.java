package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.anotations.CorrectLogin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<CorrectLogin, String> {

    @Override
    public void initialize(CorrectLogin constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        if (login.isEmpty()) {
            return true;
        }
        return !(login.isBlank());
    }
}
