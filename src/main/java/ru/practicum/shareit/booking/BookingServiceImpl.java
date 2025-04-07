package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputRequest;
import ru.practicum.shareit.exception.IncorrectOwnerException;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StartAfterEndException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.exception.NotFoundException.notFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь с идентификатором {0} не найден";
    private static final String ITEM_NOT_FOUND_MESSAGE = "Вещь с идентификатором {0} не найдена";
    private static final String ITEM_NOT_AVAILABLE_MESSAGE = "Вещь с идентификатором {0} не найдена";
    private static final String BOOKING_NOT_FOUND_MESSAGE = "Бронирование с идентификатором {0} не найдено";

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public BookingDto create(BookingInputRequest bookingInputRequest) {
        checkStartAndEnd(bookingInputRequest.getStart(), bookingInputRequest.getEnd());
        Item item = itemRepository.findById(bookingInputRequest.getItemId())
                .orElseThrow(notFoundException(ITEM_NOT_FOUND_MESSAGE, bookingInputRequest.getItemId()));
        User user = userRepository.findById(bookingInputRequest.getBookerId())
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, bookingInputRequest.getBookerId()));
        if (!item.getAvailable()) {
            throw new NotAvailableItemException(ITEM_NOT_AVAILABLE_MESSAGE, item);
        }
        Booking booking = BookingMapper.mapToModel(bookingInputRequest, user, item);
        booking = bookingRepository.save(booking);
        item.setRequestId(booking.getId());
        itemRepository.save(item);
        return BookingMapper.mapToDto(booking);
    }

    @Override
    @Transactional
    public BookingDto statusChange(long bookingId, boolean approved, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(notFoundException(BOOKING_NOT_FOUND_MESSAGE, bookingId));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new IncorrectOwnerException("Переданный идентификатор не имеет доступ к выполнению данной операции");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.save(booking);
        return BookingMapper.mapToDto(booking);
    }

    @Override
    public BookingDto specificBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(notFoundException(BOOKING_NOT_FOUND_MESSAGE, bookingId));
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotAvailableItemException("Переданный идентификатор не имеет доступ к выполнению данной операции");
        }
        return BookingMapper.mapToDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookings(BookingState state, long userId) {
        userRepository.findById(userId)
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, userId));

        LocalDateTime nowTimestamp = LocalDateTime.now();
        Sort sortByStart = Sort.by(Sort.Direction.ASC, "start");

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByBooker_Id(userId, sortByStart);
            case CURRENT ->
                    bookingRepository.findByBooker_IdAndStartBeforeAndEndAfter(userId, nowTimestamp, nowTimestamp, sortByStart); // todo: change!!! all
            case PAST -> bookingRepository.findByBooker_IdAndEndBefore(userId, nowTimestamp, sortByStart);
            case FUTURE -> bookingRepository.findByBooker_IdAndStartAfter(userId, nowTimestamp, sortByStart);
            case WAITING -> bookingRepository.findByBooker_IdAndStatus(userId, BookingStatus.WAITING, sortByStart);
            case REJECTED -> bookingRepository.findByBooker_IdAndStatus(userId, BookingStatus.REJECTED, sortByStart);
        };

        return BookingMapper.mapToDto(bookings);
    }

    @Override
    public List<BookingDto> getAllBookingsOfUserItems(BookingState state, long userId) {
        userRepository.findById(userId)
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, userId));
        long count = itemRepository.countItemsByOwnerIdEquals(userId);
        if (count == 0) {
            throw new NotFoundException("Предметы для указанного пользователя не найдены");
        }

        LocalDateTime nowTimestamp = LocalDateTime.now();
        Sort sortByStart = Sort.by(Sort.Direction.ASC, "start");

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByItem_Owner_Id(userId, sortByStart);
            case CURRENT ->
                    bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfter(userId, nowTimestamp, nowTimestamp, sortByStart); // todo: change!!! all
            case PAST -> bookingRepository.findByItem_Owner_IdAndEndBefore(userId, nowTimestamp, sortByStart);
            case FUTURE -> bookingRepository.findByItem_Owner_IdAndStartAfter(userId, nowTimestamp, sortByStart);
            case WAITING -> bookingRepository.findByItem_Owner_IdAndStatus(userId, BookingStatus.WAITING, sortByStart);
            case REJECTED ->
                    bookingRepository.findByItem_Owner_IdAndStatus(userId, BookingStatus.REJECTED, sortByStart);
        };

        return BookingMapper.mapToDto(bookings);
    }

    private void checkStartAndEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new StartAfterEndException("Начало не должно быть после конца временного промежутка или совпадать с ним");
        }
    }
}
