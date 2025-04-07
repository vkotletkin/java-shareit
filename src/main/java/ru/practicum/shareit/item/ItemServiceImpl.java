package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemEnrichedDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.NotFoundException.notFoundException;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь с идентификатором {0} не найден";
    private static final String BOOKING_NOT_FOUND_MESSAGE = "Бронирование с идентификатором {0} не найдено";

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto) {
        User user = userRepository.findById(itemDto.getOwnerId()).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        Item item = ItemMapper.mapToModel(itemDto, user);
        item = itemRepository.save(item);
        return ItemMapper.mapToDto(item);
    }


    @Override
    public Collection<ItemDto> findAllItemsByUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        return itemRepository.findByOwnerIdEquals(user.getId())
                .stream().map(ItemMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ItemEnrichedDto findById(long id) {
        // TODO: refactoring
        Item item = itemRepository.findById(id).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, id));
        List<Booking> bookings = bookingRepository.findAllByItem_Id(id);
        if (bookings.isEmpty()) {
            return ItemMapper.mapToEnrichedDto(item);
        }
        Booking lastBooking = bookings.stream()
                .filter(t -> t.getEnd().isAfter(LocalDateTime.now())).findFirst().orElse(null);

        if (lastBooking == null) {
            return ItemMapper.mapToEnrichedDto(item);
        }

        LocalDateTime nextBooking = bookings.stream()
                .map(Booking::getStart)
                .filter(u -> u.isAfter(lastBooking.getEnd())).findFirst().orElse(null);


        return ItemMapper.mapToDto(item, lastBooking.getEnd(), nextBooking);
    }


    @Override
    public ItemDto update(ItemDto itemDto) {
        User user = userRepository.findById(itemDto.getOwnerId()).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        Item item = ItemMapper.mapToModel(itemDto, user);
        Item oldItem = itemRepository.findById(item.getId()).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        Item result = update(oldItem, item);
        itemRepository.save(result);
        return ItemMapper.mapToDto(result);
    }

    @Override
    public Collection<ItemDto> findByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        } // TODO: что это!!!
        return itemRepository.findTextNameAndDescription(text)
                .stream().filter(Item::getAvailable).map(ItemMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto createComment(CommentDto commentDto, long itemId, long userId) {
        User user = userRepository.findById(userId).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        Item item = itemRepository.findById(itemId).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        // todo: ne rabotaet kakogo to huya
        if (bookingRepository.countByBooker_IdAndStatusAndEndBefore(userId, BookingStatus.APPROVED, LocalDateTime.now()) == 0) {
            throw new RuntimeException("zalupa");
        }
//        bookingRepository.findAllByBooker_Id(itemId).stream()
//                .filter(booking -> Objects.equals(booking.getBooker().getId(), userId)
//                        && (LocalDateTime.now().isAfter(booking.getEnd()) && booking.getStatus() == BookingStatus.APPROVED))
//                .findAny().orElseThrow(notFoundException("NE NAIDENO!!!"));

        Comment comment = CommentMapper.mapToModel(commentDto, item, user);
        comment = commentRepository.save(comment);
        return CommentMapper.mapToDto(comment);
    }

    private Item update(Item oldFilm, Item newFilm) {
        Item item = new Item();
        item.setId(newFilm.getId());
        item.setName(newFilm.getName() == null ? oldFilm.getName() : newFilm.getName());
        item.setDescription(newFilm.getDescription() == null ? oldFilm.getDescription() : newFilm.getDescription());
        item.setAvailable(newFilm.getAvailable());
        item.setOwner(newFilm.getOwner() == null ? oldFilm.getOwner() : newFilm.getOwner());
        item.setRequestId(newFilm.getRequestId() == null ? oldFilm.getRequestId() : newFilm.getRequestId());
        return item;
    }
}
