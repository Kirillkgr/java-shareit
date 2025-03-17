package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto dto);

    ItemDto updateItem(Long itemId, UpdateItemDto dto, Long userId);

    ExtendedItemDto getItemById(Long itemId);

    Collection<ItemDto> getAllItemsByOwner(Long id);

    Collection<ItemDto> searchItems(String text);

    CommentDto addCommentToItem(long authorId, long itemId, CommentDto comment);

    Optional<Item> findById(Long itemId);
}
