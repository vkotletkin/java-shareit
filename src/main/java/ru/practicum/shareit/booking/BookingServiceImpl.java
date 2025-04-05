package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.StartAfterEndException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Instant;

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
    public BookingDto create(BookingDto bookingDto) {
        checkStartAndEnd(bookingDto.getStart(), bookingDto.getEnd());
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(notFoundException(ITEM_NOT_FOUND_MESSAGE));
        User user = userRepository.findById(bookingDto.getUserId()).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        Booking booking = BookingMapper.mapToDto(bookingDto, user, item);
        bookingRepository.save(booking);
        return bookingDto;
    }

    private void checkStartAndEnd(Instant start, Instant end) {
        if (start.isAfter(end)) {
            throw new StartAfterEndException("Начало не должно быть после конца временного промежутка");
        }
    }
}
