package ru.practicum.shareit.booking.interfaces;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.enums.BookingSearchState;

import java.util.Collection;

public interface BookingService {

    BookingDto getBooking(long bookingId, long userId);

    BookingDto createBooking(long bookerId, CreateBookingDto bookingDto);

    BookingDto approveBooking(long bookingId, long ownerId, boolean approved);

    Collection<BookingDto> getBookings(long bookerId, BookingSearchState state);

    Collection<BookingDto> getOwnerBookings(long ownerId, BookingSearchState state);
}
