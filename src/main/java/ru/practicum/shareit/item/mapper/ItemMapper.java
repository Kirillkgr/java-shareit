package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;

/**
 * Утилитарный класс для преобразования объектов Item и ItemDto.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    /**
     * Преобразует объект ItemDto в объект Item.
     *
     * @param itemDto объект ItemDto для преобразования.
     * @return объект Item.
     */
    public static Item mapToItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(itemDto.getOwner())
                .build();
    }

    /**
     * Преобразует объект Item в объект ItemDto.
     *
     * @param item объект Item для преобразования.
     * @return объект ItemDto.
     */
    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .comments(item.getComments())
                .build();
    }

    /**
     * Преобразует объект Item и связанные с ним бронирования в объект ExtendedItemDto.
     *
     * @param item объект Item для преобразования.
     * @param itemBookings коллекция бронирований, связанных с данным предметом.
     * @return объект ExtendedItemDto.
     */
    public static ExtendedItemDto mapToExtendedItemDto(Item item, Collection<Booking> itemBookings) {
        ExtendedItemDto.ExtendedItemDtoBuilder builder = ExtendedItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(item.getComments());

        // Устанавливаем последнее бронирование, если доступно
        itemBookings.stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now()))
                .findFirst()
                .ifPresent(booking -> builder.lastBooking(booking.getEnd()));

        // Устанавливаем следующее бронирование, если доступно
        itemBookings.stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart))
                .ifPresent(booking -> builder.nextBooking(booking.getStart()));

        return builder.build();
    }
}