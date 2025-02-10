package ru.practicum.shareit.item.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MemoryItemServiceImpl implements ItemService {

    private final ItemStorage storage;

    public MemoryItemServiceImpl(@Qualifier("itemMemoryStorage") ItemStorage storage) {
        this.storage = storage;
    }

    @Override
    public ItemDto create(ItemDto dto, Long userId) {
        Item item = storage.create(ItemMapper.toItem(dto), userId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto dto, Long id, Long userId) {
        Item item = storage.findById(id);

        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new NotFoundException("Обновляемая вещь с id = " + id + " не принадлежит указанному пользователю с id = " + userId);
        }

        ItemMapper.updateItemFromDto(item, dto);

        return ItemMapper.toItemDto(storage.update(item, id));
    }

    @Override
    public ItemDto findById(Long id) {
        return ItemMapper.toItemDto(storage.findById(id));
    }

    @Override
    public List<ItemDto> findAllByUserId(Long id) {
        return storage.findAllByUserId(id)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findAvailableItemsByText(String text) {
        return storage.findByText(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        storage.delete(id);
    }
}
