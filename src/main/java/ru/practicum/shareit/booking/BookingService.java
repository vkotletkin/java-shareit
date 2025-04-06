package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingInputRequest bookingInputRequest);

    BookingDto statusChange(long bookingId, boolean approved, long userId);

    BookingDto specificBooking(long bookingId, long userId);

    List<BookingDto> getAllBookings(BookingState state, long userId);
}
