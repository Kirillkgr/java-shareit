package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

/**
 * Утилитный класс для преобразования объектов Booking и BookingDto.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    /**
     * Преобразует объект Booking в BookingDto.
     *
     * @param booking объект Booking.
     * @return объект BookingDto.
     */
    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking не может быть null");
        }

        return BookingDto.builder()
                .item(ItemMapper.mapToItemDto(booking.getItem()))
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .id(booking.getId())
                .status(booking.getStatus())
                .build();
    }

    /**
     * Преобразует CreateBookingDto в объект Booking.
     *
     * @param bookerId идентификатор пользователя, делающего бронирование.
     * @param dto      объект CreateBookingDto.
     * @return объект Booking.
     */
    public static Booking toBooking(long bookerId, CreateBookingDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("CreateBookingDto не может быть null");
        }

        return Booking.builder()
                .start(dto.start())
                .end(dto.end())
                .status(BookingStatus.WAITING)
                .booker(User.builder().id(bookerId).build())
                .item(Item.builder().id(dto.itemId()).build())
                .build();
    }
}