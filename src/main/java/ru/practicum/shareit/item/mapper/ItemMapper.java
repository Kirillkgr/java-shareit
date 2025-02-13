package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static Item toItem(ItemDto dto) {
        if (dto == null) {
            return null;
        }

        return Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .build();
    }

    public static ItemDto toItemDto(Item item) {
        if (item == null) {
            return null;
        }

        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static void updateItemFromDto(Item item, ItemDto dto) {
        if (dto == null || item == null) {
            return;
        }

        if (dto.getName() != null) {
            item.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }

        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
    }
}
