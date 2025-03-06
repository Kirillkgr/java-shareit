package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.interfaces.UserService;

import java.util.Collection;

/**
 * Контроллер для управления пользователями.
 * Обрабатывает HTTP-запросы, связанные с CRUD-операциями пользователей.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Получение списка всех пользователей.
     *
     * @return коллекция объектов UserDto.
     */
    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("Запрос на получение списка всех пользователей");
        return userService.getAllUsers();
    }

    /**
     * Создание нового пользователя.
     *
     * @param dto объект UserDto с данными нового пользователя.
     * @return объект UserDto, представляющий созданного пользователя.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserDto dto) {
        log.info("Запрос на добавление нового пользователя: {}", dto.getName());
        return userService.createUser(dto);
    }

    /**
     * Обновление данных пользователя.
     *
     * @param id  ID пользователя.
     * @param dto объект UserUpdateDto с обновленными данными.
     * @return обновленный объект UserDto.
     */
    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
        log.info("Запрос на обновление пользователя с ID = {}", id);
        return userService.updateUser(id, dto);
    }

    /**
     * Получение пользователя по ID.
     *
     * @param id ID пользователя.
     * @return объект UserDto, представляющий найденного пользователя.
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Запрос на получение пользователя с ID = {}", id);
        return userService.getUserById(id);
    }

    /**
     * Удаление пользователя по ID.
     *
     * @param id ID пользователя.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("Запрос на удаление пользователя с ID = {}", id);
        userService.deleteUser(id);
    }
}