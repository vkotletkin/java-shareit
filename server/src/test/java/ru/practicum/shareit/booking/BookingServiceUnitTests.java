package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceUnitTests {


    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User owner;
    private User booker;
    private Item item;
    private Booking booking;
    private BookingInputRequest bookingInputRequest;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .name("Owner")
                .email("owner@example.com")
                .build();

        booker = User.builder()
                .id(2L)
                .name("Booker")
                .email("booker@example.com")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .owner(owner)
                .build();

        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        bookingInputRequest = BookingInputRequest.builder()
                .itemId(1L)
                .bookerId(2L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    void testCreateWhenItemNotFoundShouldThrowNotFoundException() {

        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingInputRequest));
    }

    @Test
    void testCreateWhenStartAfterEndShouldThrowStartAfterEndException() {

        bookingInputRequest.setStart(LocalDateTime.now().plusDays(2));
        bookingInputRequest.setEnd(LocalDateTime.now().plusDays(1));

        assertThrows(StartAfterEndException.class, () -> bookingService.create(bookingInputRequest));
    }

    @Test
    void testCreateWhenStartEqualsEndShouldThrowStartAfterEndException() {

        LocalDateTime now = LocalDateTime.now();
        bookingInputRequest.setStart(now);
        bookingInputRequest.setEnd(now);

        assertThrows(StartAfterEndException.class, () -> bookingService.create(bookingInputRequest));
    }

    @Test
    void testStatusChangeWhenApprovedShouldUpdateStatus() {

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.statusChange(1L, true, 1L);

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testStatusChangeWhenRejectedShouldUpdateStatus() {

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.statusChange(1L, false, 1L);

        assertNotNull(result);
        assertEquals(BookingStatus.REJECTED, result.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testStatusChangeWhenBookingNotFoundShouldThrowNotFoundException() {

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.statusChange(1L, true, 1L));
    }

    @Test
    void testStatusChangeWhenUserNotOwnerShouldThrowIncorrectOwnerException() {

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(IncorrectOwnerException.class, () -> bookingService.statusChange(1L, true, 2L));
    }

    @Test
    void testStatusChangeWhenBookingFoundShouldReturnBookingDto() {

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.specificBooking(1L, 1L);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void testSpecificBookingWhenBookingNotFoundShouldThrowNotFoundException() {

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.specificBooking(1L, 1L));
    }

    @Test
    void testSpecificBookingWhenUserNotOwnerOrBookerShouldThrowNotAvailableItemException() {

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotAvailableItemException.class, () -> bookingService.specificBooking(1L, 3L));
    }

    @Test
    void testGetAllBookingsWhenStateAllShouldReturnAllBookings() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBooker_Id(anyLong(), any(Sort.class))).thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookings(BookingState.ALL, 2L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBookingsWhenStateCurrentShouldReturnCurrentBookings() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBooker_IdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookings(BookingState.CURRENT, 2L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBookingsWhenStatePastShouldReturnPastBookings() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBooker_IdAndEndBefore(anyLong(), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookings(BookingState.PAST, 2L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBookingsWhenStateFutureShouldReturnFutureBookings() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBooker_IdAndStartAfter(anyLong(), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookings(BookingState.FUTURE, 2L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBookingsWhenStateWaitingShouldReturnWaitingBookings() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBooker_IdAndStatus(anyLong(), any(BookingStatus.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookings(BookingState.WAITING, 2L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBookingsWhenStateRejectedShouldReturnRejectedBookings() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBooker_IdAndStatus(anyLong(), any(BookingStatus.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookings(BookingState.REJECTED, 2L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBookingWhenUserNotFoundShouldThrowNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getAllBookings(BookingState.ALL, 1L));
    }

    @Test
    void testGetAllBookingsOfUserItemsWhenStateAllShouldReturnAllBookings() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.countItemsByOwnerIdEquals(anyLong())).thenReturn(1L);
        when(bookingRepository.findByItem_Owner_Id(anyLong(), any(Sort.class))).thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookingsOfUserItems(BookingState.ALL, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBookingsOfUserItemsWhenStateCurrentShouldReturnCurrentBookings() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.countItemsByOwnerIdEquals(anyLong())).thenReturn(1L);
        when(bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookingsOfUserItems(BookingState.CURRENT, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBookingsOfUserItemsWhenStatePastShouldReturnPastBookings() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.countItemsByOwnerIdEquals(anyLong())).thenReturn(1L);
        when(bookingRepository.findByItem_Owner_IdAndEndBefore(anyLong(), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookingsOfUserItems(BookingState.PAST, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBookingsOfUserItemsWhenStateFutureShouldReturnFutureBookings() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.countItemsByOwnerIdEquals(anyLong())).thenReturn(1L);
        when(bookingRepository.findByItem_Owner_IdAndStartAfter(anyLong(), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookingsOfUserItems(BookingState.FUTURE, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBookingsOfUserItemsWhenStateWaitingShouldReturnWaitingBookings() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.countItemsByOwnerIdEquals(anyLong())).thenReturn(1L);
        when(bookingRepository.findByItem_Owner_IdAndStatus(anyLong(), any(BookingStatus.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookingsOfUserItems(BookingState.WAITING, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBookingsOfUserItemsWhenStateRejectedShouldReturnRejectedBookings() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.countItemsByOwnerIdEquals(anyLong())).thenReturn(1L);
        when(bookingRepository.findByItem_Owner_IdAndStatus(anyLong(), any(BookingStatus.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookingsOfUserItems(BookingState.REJECTED, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBookingsOfUserItemsWhenUserNotFoundShouldThrowNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getAllBookingsOfUserItems(BookingState.ALL, 1L));
    }

    @Test
    void testGetAllBookingsOfUserItemsWhenNoItemsShouldThrowNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.countItemsByOwnerIdEquals(anyLong())).thenReturn(0L);

        assertThrows(NotFoundException.class, () -> bookingService.getAllBookingsOfUserItems(BookingState.ALL, 1L));
    }
}