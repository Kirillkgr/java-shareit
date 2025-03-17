package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateBookingDto(@NotNull (message = "Идентификатор вещи не может быть пустым") Long itemId,
                               @NotNull (message = "Дата начала бронирования не может быть пустой") LocalDateTime start,
                               @Future (message = "Дата окончания бронирования не может быть меньше или равна текущей") @NotNull (message = "Дата окончания бронирования не может быть пустой") LocalDateTime end) {

}
