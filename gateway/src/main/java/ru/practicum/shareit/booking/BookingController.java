package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputRequest;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String USER_IDENTIFICATOR_HEADER_NAME = "X-Sharer-User-Id";

    private final BookingClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookingInputRequest bookingInputRequest,
                                         @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return client.create(bookingInputRequest, userId);
    }

    @PatchMapping("/{booking-id}")
    public ResponseEntity<Object> statusChange(@PathVariable(name = "booking-id") long bookingId,
                                               @RequestParam boolean approved,
                                               @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return client.statusChange(bookingId, approved, userId);
    }

    @GetMapping("/{booking-id}")
    public ResponseEntity<Object> get(@PathVariable(name = "booking-id") long bookingId,
                                      @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) long userId) {
        return client.specificBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsOfUser(@RequestParam(defaultValue = "ALL") BookingState state,
                                                       @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) long userId) {
        return client.getAllBookings(state, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsOfUserItems(@RequestParam(defaultValue = "ALL") BookingState state,
                                                            @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) long userId) {
        return client.getAllBookingsOfUserItems(state, userId);
    }
}
