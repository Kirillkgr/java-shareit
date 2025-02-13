package ru.practicum.shareit.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MemoryUserServiceImpl implements UserService {

    private final UserStorage storage;

    public MemoryUserServiceImpl(@Qualifier("userMemoryStorage") UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public UserDto create(UserDto dto) {
        User user = storage.create(UserMapper.mapDtoToUser(dto));
        return UserMapper.mapUserToDto(user);
    }

    @Override
    public UserDto update(UserDto dto, Long id) {
        User user = storage.findById(id);

        if (dto.getEmail() != null) {
            user = user.toBuilder().email(dto.getEmail()).build();
        }
        if (dto.getName() != null) {
            user = user.toBuilder().name(dto.getName()).build();
        }

        if (dto.getEmail() == null && dto.getName() == null) {
            log.error("Полученный UserDto не содержит ни имени, ни email");
            throw new ValidationException("Полученный UserDto не содержит ни имени, ни email");
        }

        return UserMapper.mapUserToDto(storage.update(user, id));
    }

    @Override
    public void delete(Long id) {
        storage.delete(id);
    }

    @Override
    public List<UserDto> findAll() {
        return storage.findAll()
                .stream()
                .map(UserMapper::mapUserToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        return UserMapper.mapUserToDto(storage.findById(id));
    }
}
