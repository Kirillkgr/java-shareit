package ru.practicum.shareit.user.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exeptions.ConflictException;
import ru.practicum.shareit.exeptions.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component("userMemoryStorage")
public class MemoryUserStorageImpl implements UserStorage {

    private final AtomicLong userId = new AtomicLong(1);
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        log.info("Создание нового пользователя = {}", user);
        emailExistCheck(user);

        long id = userId.getAndIncrement();
        user = user.toBuilder()
                .id(id)
                .build();

        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user, Long id) {
        log.info("Обновление пользователя с id = {}", id);
        if (user.getEmail() != null) {
            emailExistCheck(user);
        }

        if (!users.containsKey(id)) {
            log.error("Обновляемый пользователь не существует");
            throw new NotFoundException("Обновляемый пользователь не существует");
        }

        users.put(id, user);
        return user;
    }

    @Override
    public void delete(Long id) {
        log.info("Удаление пользователя с id = {}", id);
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не существует");
        }
        users.remove(id);
    }

    @Override
    public List<User> findAll() {
        log.info("Получение всех пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(Long id) {
        log.info("Получение пользователя с id = {}", id);
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не существует");
        }
        return users.get(id);
    }

    private void emailExistCheck(User user) {
        users.values().forEach(existingUser -> {
            if (existingUser.getEmail().equals(user.getEmail()) && !Objects.equals(existingUser.getId(), user.getId())) {
                log.error("Пользователь с указанным email = {} уже существует", user.getEmail());
                throw new ConflictException("Пользователь с указанным email существует");
            }
        });
    }
}
