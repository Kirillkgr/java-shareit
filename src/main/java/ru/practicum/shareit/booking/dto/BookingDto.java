package ru.practicum.shareit.booking.dto;

/**
 * TODO Sprint add-bookings.
 */
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;
    private Long itemId;
    private Long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
}
