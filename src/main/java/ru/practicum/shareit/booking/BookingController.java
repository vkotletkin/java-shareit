package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String USER_IDENTIFICATOR_HEADER_NAME = "X-Sharer-User-Id";
    private final BookingService service;

    @PostMapping
    public BookingDto create(@RequestBody BookingInputRequest bookingInputRequest,
                             @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        bookingInputRequest.setBookerId(userId);
        return service.create(bookingInputRequest);
    }
}
