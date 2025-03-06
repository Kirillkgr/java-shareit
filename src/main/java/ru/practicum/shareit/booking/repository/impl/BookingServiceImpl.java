package ru.practicum.shareit.booking.repository.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.enums.BookingSearchState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemService;
import ru.practicum.shareit.user.interfaces.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Реализация интерфейса BookingService.
 * Этот сервис обрабатывает операции, связанные с бронированием вещей.
 */
@Slf4j
@Service
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    ItemService itemService;
    UserService userRepository;
    BookingRepository bookingRepository;

    /**
     * Создает новое бронирование.
     *
     * @param bookerId   ID пользователя, совершающего бронирование
     * @param bookingDto детали бронирования
     * @return созданное бронирование в виде BookingDto
     */
    @Override
    public BookingDto createBooking(long bookerId, CreateBookingDto bookingDto) {
        Booking booking = BookingMapper.toBooking(bookerId, bookingDto);
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", bookerId)));
        Long itemId = booking.getItem().getId();
        Item item = itemService.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id = %d не найдена", itemId)));
        if (! item.getAvailable()) {
            throw new BookingException(String.format("Вещь с id = %d недоступна для бронирования", itemId));
        }
        booking.setBooker(booker);
        booking.setItem(item);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    /**
     * Получает бронирование по его ID.
     *
     * @param bookingId ID бронирования
     * @param userId    ID пользователя, запрашивающего бронирование
     * @return бронирование в виде BookingDto
     */
    @Override
    public BookingDto getBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронь с id = %d не найдена", bookingId)));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new AccessDeniedException(String.format("Доступ к брони с id = %d запрещен", bookingId));
        }
        return BookingMapper.toBookingDto(booking);
    }

    /**
     * Получает все бронирования, сделанные пользователем, с фильтрацией по состоянию.
     *
     * @param bookerId ID пользователя
     * @param state    состояние для фильтрации бронирований
     * @return коллекция объектов BookingDto
     */
    @Override
    public Collection<BookingDto> getBookings(long bookerId, BookingSearchState state) {
        if (! userRepository.existsById(bookerId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", bookerId));
        }
        Collection<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByBookerIdOrderByStartDesc(bookerId);
            case PAST -> bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
            case FUTURE -> bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
            case CURRENT ->
                    bookingRepository.findByBookerIdAndStartAfterAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
        };
        return bookings.stream().map(BookingMapper::toBookingDto).toList();
    }

    /**
     * Получает все бронирования для вещей, принадлежащих пользователю, с фильтрацией по состоянию.
     *
     * @param ownerId ID пользователя
     * @param state   состояние для фильтрации бронирований
     * @return коллекция объектов BookingDto
     */
    @Override
    public Collection<BookingDto> getOwnerBookings(long ownerId, BookingSearchState state) {
        if (! userRepository.existsById(ownerId)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", ownerId));
        }
        Collection<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);
            case PAST -> bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
            case FUTURE ->
                    bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now());
            case CURRENT ->
                    bookingRepository.findByItemOwnerIdAndStartAfterAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING ->
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
        };
        return bookings.stream().map(BookingMapper::toBookingDto).toList();
    }

    /**
     * Подтверждает или отклоняет бронирование.
     *
     * @param bookingId ID бронирования
     * @param ownerId   ID владельца
     * @param approved  флаг подтверждения бронирования
     * @return обновленное бронирование в виде BookingDto
     */
    @Override
    public BookingDto approveBooking(long bookingId, long ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронь с id = %d не найдена", bookingId)));
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new AccessDeniedException(String.format("Подтверждение брони с id = %d пользователем с id = %d запрещено", bookingId, ownerId));
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }
}
