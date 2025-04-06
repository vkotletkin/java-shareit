package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.common.dto.CatalogMapper;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static Booking mapToModel(BookingInputRequest bookingInputRequest, User user, Item item) {
        Booking booking = new Booking();
        booking.setId(bookingInputRequest.getId());
        booking.setStart(bookingInputRequest.getStart());
        booking.setEnd(bookingInputRequest.getEnd());
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(bookingInputRequest.getStatus() == null ? BookingStatus.WAITING : bookingInputRequest.getStatus());
        return booking;
    }

    // TODO: сделай join fetch
    public static BookingDto mapToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(CatalogMapper.mapToDto(booking.getBooker()))
                .item(CatalogMapper.mapToDto(booking.getItem()))
                .status(booking.getStatus())
                .build();
    }
}
