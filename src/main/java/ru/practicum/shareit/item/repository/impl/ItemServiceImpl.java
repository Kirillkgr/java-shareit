package ru.practicum.shareit.item.repository.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.CommentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.ItemService;
import ru.practicum.shareit.user.interfaces.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    UserService userRepository;
    ItemRepository itemRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto dto) {
        userRepository.findById(userId);
        log.debug("Добавление новой вещи с именем: {} пользователю с id = {}", dto.getName(), userId);
        Item item = ItemMapper.mapToItem(dto);
        item.setOwner(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", userId))));
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long itemId, UpdateItemDto dto, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Вещь с id = %d не найдена", itemId)));
        if (! item.getOwner().getId().equals(userId)) {
            log.error("Пользователь с id = {} не владелец вещи с id = {}", userId, itemId);
            throw new AccessDeniedException(String.format("Пользователь с id = %s не владелец вещи с id = %s", userId, itemId));
        }
        log.debug("Обновление вещи с id = {} пользователя с id = {}", itemId, userId);
        if (dto.name() != null && ! dto.name().isBlank()) {
            item.setName(dto.name());
        }
        if (dto.description() != null && ! dto.description().isBlank()) {
            item.setDescription(dto.description());
        }
        if (dto.available() != null) {
            item.setAvailable(dto.available());
        }
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ExtendedItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Вещь с id = %d не найдена", itemId)));

        Collection<Booking> bookings = bookingRepository.findByItemId(itemId);

        return ItemMapper.mapToExtendedItemDto(item, bookings);
    }

    @Override
    public Collection<ItemDto> getAllItemsByOwner(Long id) {
        log.debug("Получение списка всех вещей пользовтеля с id = {}", id);
        return itemRepository.findByOwnerId(id).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return  findByNameOrDescriptionContainingIgnoreCase(text).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public CommentDto addCommentToItem(long authorId, long itemId, CommentDto commentDto) {
        Comment comment = CommentMapper.toComment(authorId, itemId, commentDto);
        Collection<Booking> authorBookings = bookingRepository.findByBookerIdAndItemId(comment.getAuthor().getId(), comment.getItem().getId());

        if (authorBookings.isEmpty() || authorBookings.stream()
                .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                .filter(booking -> booking.getItem().getId() == itemId)
                .toList().isEmpty()) {
            throw new CommentException(String.format("Пользователь с id = %d не может оставить комментарии к вещи с id = %d", comment.getAuthor().getId(), comment.getItem().getId()));
        }

        comment.setItem(itemRepository.findById(comment.getItem().getId()).orElseThrow(() -> new NotFoundException(String.format("Вещь с id = %d не найдена", comment.getItem().getId()))));
        comment.setAuthor(userRepository.findById(comment.getAuthor().getId()).orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", comment.getAuthor().getId()))));

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        return itemRepository.findById(itemId);
    }

    List<Item> findByNameOrDescriptionContainingIgnoreCase(String searchText) {
        return itemRepository.findByNameOrDescriptionContainingIgnoreCase(searchText);
    }
}
