package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputRequest;
import ru.practicum.shareit.common.mapper.CatalogMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

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

    public static List<BookingDto> mapToDto(List<Booking> bookings) {
        List<BookingDto> dtos = new ArrayList<>();
        for (Booking booking : bookings) {
            dtos.add(mapToDto(booking));
        }
        return dtos;
    }
}
