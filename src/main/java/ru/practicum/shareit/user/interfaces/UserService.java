package ru.practicum.shareit.user.interfaces;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Collection<UserDto> getAllUsers();

    UserDto createUser(UserDto dto);

    UserDto updateUser(Long id, UserUpdateDto dto);

    UserDto getUserById(Long id);

    void deleteUser(Long id);

    Optional<User> findById(Long userId);

    boolean existsById(long bookerId);
}
