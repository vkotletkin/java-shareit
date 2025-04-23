package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputRequest;
import ru.practicum.shareit.exception.IncorrectOwnerException;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StartAfterEndException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.BookingStatus.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceUnitTests {

    private final User owner = User.builder().id(1L).name("Owner").build();
    private final User booker = User.builder().id(2L).name("Booker").build();
    private final Item availableItem = Item.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(owner)
            .build();
    private final Item unavailableItem = Item.builder()
            .id(2L)
            .name("Unavailable Item")
            .available(false)
            .owner(owner)
            .build();
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void create_whenItemNotFound_thenThrowNotFoundException() {
        BookingInputRequest request = BookingInputRequest.builder()
                .itemId(999L)
                .bookerId(2L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Вещь с идентификатором 999 не найдена");
    }

    @Test
    void create_whenStartEqualsEnd_thenThrowStartAfterEndException() {
        LocalDateTime now = LocalDateTime.now();
        BookingInputRequest request = BookingInputRequest.builder()
                .itemId(1L)
                .bookerId(2L)
                .start(now)
                .end(now)
                .build();

        assertThatThrownBy(() -> bookingService.create(request))
                .isInstanceOf(StartAfterEndException.class);
    }

    // ========== STATUS CHANGE ==========
    @Test
    void statusChange_whenApproved_thenStatusApproved() {
        Booking booking = Booking.builder()
                .id(1L)
                .status(WAITING)
                .item(availableItem)
                .booker(booker)
                .build();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.statusChange(1L, true, 1L);

        assertThat(result.getStatus()).isEqualTo(APPROVED);
        verify(bookingRepository).save(booking);
    }

    @Test
    void statusChange_whenRejected_thenStatusRejected() {
        Booking booking = Booking.builder()
                .id(1L)
                .status(WAITING)
                .item(availableItem)
                .booker(booker)
                .build();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.statusChange(1L, false, 1L);

        assertThat(result.getStatus()).isEqualTo(REJECTED);
        verify(bookingRepository).save(booking);
    }

    @Test
    void statusChange_whenBookingNotFound_thenThrowNotFoundException() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.statusChange(999L, true, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Бронирование с идентификатором 999 не найдено");
    }

    @Test
    void statusChange_whenNotOwner_thenThrowIncorrectOwnerException() {
        Booking booking = Booking.builder()
                .id(1L)
                .status(WAITING)
                .item(availableItem)
                .booker(booker)
                .build();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.statusChange(1L, true, 999L))
                .isInstanceOf(IncorrectOwnerException.class);
    }

    // ========== SPECIFIC BOOKING ==========
    @Test
    void specificBooking_whenOwner_thenReturnBookingDto() {
        Booking booking = Booking.builder()
                .id(1L)
                .item(availableItem)
                .booker(booker)
                .build();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.specificBooking(1L, 1L); // owner

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void specificBooking_whenBooker_thenReturnBookingDto() {
        Booking booking = Booking.builder()
                .id(1L)
                .item(availableItem)
                .booker(booker)
                .build();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.specificBooking(1L, 2L); // booker

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void specificBooking_whenNotOwnerOrBooker_thenThrowNotAvailableItemException() {
        Booking booking = Booking.builder()
                .id(1L)
                .item(availableItem)
                .booker(booker)
                .build();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.specificBooking(1L, 999L))
                .isInstanceOf(NotAvailableItemException.class);
    }

    // ========== GET ALL BOOKINGS (BY BOOKER) ==========


    // ========== GET ALL BOOKINGS (BY OWNER) ==========

    @Test
    void getAllBookingsOfUserItems_whenNoItems_thenThrowNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.countItemsByOwnerIdEquals(1L)).thenReturn(0L);

        assertThatThrownBy(() -> bookingService.getAllBookingsOfUserItems(BookingState.ALL, 1L))
                .isInstanceOf(NotFoundException.class);
    }
}