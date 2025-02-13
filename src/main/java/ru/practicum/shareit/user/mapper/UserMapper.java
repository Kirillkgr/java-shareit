package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Slf4j
@UtilityClass
public class UserMapper {

    public User mapDtoToUser(UserDto dto) {
        log.debug("Mapping UserDto to User: {}", dto);
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .name(dto.getName())
                .build();
    }

    public UserDto mapUserToDto(User user) {
        log.debug("Mapping User to UserDto: {}", user);
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}