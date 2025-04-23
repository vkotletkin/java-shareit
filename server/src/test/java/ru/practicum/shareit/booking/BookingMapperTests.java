package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingMapperTests {

    @Test
    void testShouldMapInputRequestToModel() {

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingInputRequest request = BookingInputRequest.builder()
                .id(1L)
                .start(start)
                .end(end)
                .status(BookingStatus.APPROVED)
                .build();

        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(1L);

        Booking booking = BookingMapper.mapToModel(request, user, item);

        assertNotNull(booking);
        assertEquals(request.getId(), booking.getId());
        assertEquals(request.getStart(), booking.getStart());
        assertEquals(request.getEnd(), booking.getEnd());
        assertEquals(user, booking.getBooker());
        assertEquals(item, booking.getItem());
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
    }

    @Test
    void testShouldSetWaitingStatusWhenNull() {

        BookingInputRequest request = BookingInputRequest.builder()
                .status(null)
                .build();

        User user = new User();
        Item item = new Item();


        Booking booking = BookingMapper.mapToModel(request, user, item);


        assertEquals(BookingStatus.WAITING, booking.getStatus());
    }

    @Test
    void testShouldMapModelToDto() {

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@email.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);

        BookingDto dto = BookingMapper.mapToDto(booking);

        assertNotNull(dto);
        assertEquals(booking.getId(), dto.getId());
        assertEquals(booking.getStart(), dto.getStart());
        assertEquals(booking.getEnd(), dto.getEnd());
        assertEquals(booking.getStatus(), dto.getStatus());
        assertNotNull(dto.getBooker());
        assertEquals(user.getId(), dto.getBooker().getId());
        assertNotNull(dto.getItem());
        assertEquals(item.getId(), dto.getItem().getId());
    }

    @Test
    void testShouldHandleListOfBookings() {

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@email.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);


        List<Booking> bookings = List.of(booking);

        List<BookingDto> dtos = BookingMapper.mapToDto(bookings);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(booking.getId(), dtos.getFirst().getId());
    }

    @Test
    void testShouldReturnEmptyListForEmptyInput() {

        List<Booking> emptyList = List.of();

        List<BookingDto> dtos = BookingMapper.mapToDto(emptyList);

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}
