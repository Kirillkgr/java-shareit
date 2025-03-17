package ru.practicum.shareit.user.interfaces.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.interfaces.UserRepository;
import ru.practicum.shareit.user.interfaces.UserService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

/**
 * Реализация сервиса управления пользователями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Получение списка всех пользователей.
     *
     * @return коллекция пользователей в виде DTO.
     */
    @Override
    public Collection<UserDto> getAllUsers() {
        log.debug("Получение списка всех пользователей");
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    /**
     * Создание нового пользователя.
     *
     * @param dto DTO пользователя.
     * @return созданный пользователь в виде DTO.
     */
    @Override
    public UserDto createUser(UserDto dto) {
        log.debug("Добавление нового пользователя с именем: {}", dto.getName());
        User user = UserMapper.toUser(dto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    /**
     * Обновление данных пользователя.
     *
     * @param userId ID пользователя.
     * @param dto    DTO с обновляемыми данными.
     * @return обновленный пользователь в виде DTO.
     */
    @Override
    public UserDto updateUser(Long userId, UserUpdateDto dto) {
        validateId(userId);
        User updateUser = UserMapper.toUserFromUpdateDto(dto);
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с ID = %d не найден", userId)));

        if (updateUser.getName() != null && ! updateUser.getName().isBlank()) {
            existingUser.setName(updateUser.getName());
        }

        if (updateUser.getEmail() != null && ! updateUser.getEmail().isBlank()) {
            existingUser.setEmail(updateUser.getEmail());
        }

        log.debug("Обновление пользователя с ID = {}", userId);
        return UserMapper.toUserDto(userRepository.save(existingUser));
    }

    /**
     * Получение пользователя по ID.
     *
     * @param id ID пользователя.
     * @return пользователь в виде DTO.
     */
    @Override
    public UserDto getUserById(Long id) {
        log.debug("Получение пользователя с ID = {}", id);
        return UserMapper.toUserDto(userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с ID = %d не найден", id))));
    }

    /**
     * Удаление пользователя по ID.
     *
     * @param id ID пользователя.
     */
    @Override
    public void deleteUser(Long id) {
        validateId(id);
        log.debug("Удаление пользователя с ID = {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public boolean existsById(long bookerId) {
        return userRepository.existsById(bookerId);
    }

    /**
     * Валидация идентификатора пользователя.
     *
     * @param id ID пользователя.
     */
    private void validateId(Long id) {
        if (id == null) {
            log.error("ID пользователя не указан");
            throw new NotFoundException("ID пользователя не может быть null");
        }
    }
}