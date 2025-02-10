package ru.practicum.shareit.item.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("itemMemoryStorage")
public class MemoryItemStorageImpl implements ItemStorage {

    private final UserService userService;
    private final Map<Long, Item> items = new HashMap<>();
    private long itemId = 1;

    @Autowired
    public MemoryItemStorageImpl(UserService userService) {
        this.userService = userService;
    }

    private synchronized long generateId() {
        return itemId++;
    }

    @Override
    public Item create(Item item, Long userId) {
        log.info("Adding new item: {}", item);
        User user = UserMapper.mapDtoToUser(userService.findById(userId));

        Item newItem = Item.builder()
                .id(generateId())
                .owner(user)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();

        items.put(newItem.getId(), newItem);
        return newItem;
    }

    @Override
    public Item update(Item newItem, Long id) {
        log.info("Updating item: {}", id);
        items.put(id, newItem);
        return newItem;
    }

    @Override
    public Item findById(Long id) {
        log.info("Finding item by id: {}", id);
        return items.getOrDefault(id, null);
    }

    @Override
    public List<Item> findAllByUserId(Long userId) {
        log.info("Finding items by user id: {}", userId);
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByText(String text) {
        log.info("Searching for items with text: {}", text);
        String upperText = text.toUpperCase();

        return items.values().stream()
                .filter(item -> !text.isBlank() && item.getName().toUpperCase().contains(upperText) && item.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting item with id: {}", id);
        items.remove(id);
    }
}
