DELETE FROM likes;
DELETE FROM film_genre;
DELETE FROM friends;
DELETE FROM users;
DELETE FROM film;

ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;
ALTER TABLE film ALTER COLUMN film_id RESTART WITH 1;

MERGE INTO genre (genre_id, name)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

MERGE INTO mpa (mpa_id, name)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');