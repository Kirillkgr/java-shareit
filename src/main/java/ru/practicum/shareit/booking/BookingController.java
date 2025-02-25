package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/booker/{bookerId}")
    public ResponseEntity<List<BookingDto>> getBookingsByBooker(@PathVariable Long bookerId) {
        return ResponseEntity.ok(bookingService.getBookingsByBooker(bookerId));
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<BookingDto>> getBookingsByItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(bookingService.getBookingsByItem(itemId));
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestBody BookingRequestDto request,
            @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        log.warn(request.toString()+"_:_"+bookerId);
        return ResponseEntity.ok(bookingService.createBooking(request, bookerId));
    }
}
