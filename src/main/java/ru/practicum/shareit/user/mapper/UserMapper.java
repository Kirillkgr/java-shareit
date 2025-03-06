package ru.practicum.shareit.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

/**
 * Утилитный класс для маппинга объектов User и DTO.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    /**
     * Преобразует объект UserDto в сущность User.
     *
     * @param userDto объект UserDto.
     * @return сущность User.
     */
    public static User toUser(UserDto userDto) {
        if (userDto == null) {
            throw new IllegalArgumentException("UserDto не может быть null");
        }

        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    /**
     * Преобразует сущность User в объект UserDto.
     *
     * @param user объект User.
     * @return объект UserDto.
     */
    public static UserDto toUserDto(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User не может быть null");
        }

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Преобразует объект UserUpdateDto в сущность User.
     *
     * @param userUpdateDto объект UserUpdateDto.
     * @return сущность User (с обновленными данными).
     */
    public static User toUserFromUpdateDto(UserUpdateDto userUpdateDto) {
        if (userUpdateDto == null) {
            throw new IllegalArgumentException("UserUpdateDto не может быть null");
        }

        return User.builder()
                .name(userUpdateDto.getName())
                .email(userUpdateDto.getEmail())
                .build();
    }
}