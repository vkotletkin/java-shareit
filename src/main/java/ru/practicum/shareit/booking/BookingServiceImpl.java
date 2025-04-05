package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.StartAfterEndException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static ru.practicum.shareit.exception.NotFoundException.notFoundException;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь с идентификатором {0} не найден";
    private static final String ITEM_NOT_FOUND_MESSAGE = "Вещь с идентификатором {0} не найдена";

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    public BookingDto create(BookingInputRequest bookingInputRequest) {
        checkStartAndEnd(bookingInputRequest.getStart(), bookingInputRequest.getEnd());
        Item item = itemRepository.findById(bookingInputRequest.getItemId()).orElseThrow(notFoundException(ITEM_NOT_FOUND_MESSAGE));
        User user = userRepository.findById(bookingInputRequest.getBookerId()).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        Booking booking = BookingMapper.mapToModel(bookingInputRequest, user, item);
        booking = bookingRepository.save(booking);
        return BookingMapper.mapToDto(booking);
    }

    private void checkStartAndEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new StartAfterEndException("Начало не должно быть после конца временного промежутка");
        }
    }
}
