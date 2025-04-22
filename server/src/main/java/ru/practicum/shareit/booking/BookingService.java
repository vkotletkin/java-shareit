package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputRequest;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingInputRequest bookingInputRequest);

    BookingDto statusChange(long bookingId, boolean approved, long userId);

    BookingDto specificBooking(long bookingId, long userId);

    List<BookingDto> getAllBookings(BookingState state, long userId);

    List<BookingDto> getAllBookingsOfUserItems(BookingState state, long userId);
}
