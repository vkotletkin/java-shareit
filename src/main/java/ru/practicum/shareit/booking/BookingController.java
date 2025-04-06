package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String USER_IDENTIFICATOR_HEADER_NAME = "X-Sharer-User-Id";
    private final BookingService service;

    @PostMapping
    public BookingDto create(@RequestBody @Valid BookingInputRequest bookingInputRequest,
                             @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        bookingInputRequest.setBookerId(userId);
        return service.create(bookingInputRequest);
    }

    @PatchMapping("/{booking-id}")
    public BookingDto statusChange(@PathVariable(name = "booking-id") long bookingId,
                                   @RequestParam boolean approved,
                                   @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return service.statusChange(bookingId, approved, userId);
    }

    @GetMapping("/{booking-id}")
    public BookingDto get(@PathVariable(name = "booking-id") long bookingId,
                          @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) long userId) {
        return service.specificBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsOfUser(@RequestParam(defaultValue = "ALL") BookingState state,
                                                 @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) long userId) {
        return service.getAllBookings(state, userId);
    }
}
