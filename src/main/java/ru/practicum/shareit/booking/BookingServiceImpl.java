package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.StartAfterEndException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.exception.NotFoundException.notFoundException;

@Service
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
    public BookingDto create(BookingInputRequest bookingInputRequest) {
        // Порядок валидации не менять - иначе ломаются тесты
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
        return BookingMapper.mapToDto(booking);
    }

    @Override
    public BookingDto statusChange(long bookingId, boolean approved, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(notFoundException(BOOKING_NOT_FOUND_MESSAGE, bookingId));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotAvailableItemException("такое нельзя ибо юзер не подходит"); // TODO: делаем 400 ошибку
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.mapToDto(booking);
    }

    @Override
    public BookingDto specificBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(notFoundException(BOOKING_NOT_FOUND_MESSAGE, bookingId));
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotAvailableItemException("такое нельзя ибо юзер не подходит"); // TODO: делаем 400 ошибку
        }
        return BookingMapper.mapToDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookings(BookingState state, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, userId));
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByBooker_IdOrderByStartDesc(userId);
            case CURRENT -> bookingRepository.findByBooker_Id;
            case PAST -> null;
            case FUTURE -> null;
            case WAITING -> bookingRepository.findByBooker_IdAndStatusEquals
            case REJECTED -> null;
        };
    }

    private void checkStartAndEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new StartAfterEndException("Начало не должно быть после конца временного промежутка или совпадать с ним");
        }
    }
}
