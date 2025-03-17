package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder (toBuilder = true)
@Data
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public final class ExtendedItemDto {

    long id;

    String name;

    Boolean available;

    String description;

    Collection<String> comments;

    LocalDateTime lastBooking;

    LocalDateTime nextBooking;
}
