package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String USER_IDENTIFICATOR_HEADER_NAME = "X-Sharer-User-Id";
    private final BookingService service;

    @PostMapping
    public BookingDto create(@RequestBody BookingDto bookingDto,
                             @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        bookingDto.setUserId(userId);
        return service.create(bookingDto);
    }
}
