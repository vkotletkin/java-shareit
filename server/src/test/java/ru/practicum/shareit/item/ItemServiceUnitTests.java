package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemEnrichedDto;
import ru.practicum.shareit.item.model.Comment;
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
public class ItemServiceUnitTests {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;


    @Test
    void findAllItemsByUser_shouldReturnItemsList() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.findByOwnerIdEquals(anyLong())).thenReturn(List.of(item));

        List<ItemDto> result = (List<ItemDto>) itemService.findAllItemsByUser(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(userRepository).findById(1L);
        verify(itemRepository).findByOwnerIdEquals(1L);
    }

    @Test
    void findById_shouldReturnEnrichedItemWithBookingsAndComments() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));

        Comment comment = new Comment();
        comment.setText("Test comment");

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItem_Id(anyLong())).thenReturn(List.of(booking));
        when(commentRepository.findByItem_Id(anyLong())).thenReturn(List.of(comment));

        ItemEnrichedDto result = itemService.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getLastBooking());
        assertFalse(result.getComments().isEmpty());
        verify(itemRepository).findById(1L);
        verify(bookingRepository).findAllByItem_Id(1L);
        verify(commentRepository).findByItem_Id(1L);
    }


    @Test
    void findByText_shouldReturnEmptyListForBlankText() {
        List<ItemDto> result = (List<ItemDto>) itemService.findByText(" ");

        assertTrue(result.isEmpty());
        verify(itemRepository, never()).findTextNameAndDescription(anyString());
    }

    @Test
    void createComment_shouldSaveAndReturnComment() {
        CommentDto inputDto = CommentDto.builder().build();
        inputDto.setText("Test comment");

        User author = new User();
        author.setId(1L);
        author.setName("Test Author");
        author.setEmail("test@test.com");

        Item item = new Item();
        item.setId(1L);
        item.setOwner(author);

        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setText("Test comment");
        savedComment.setUser(author);
        savedComment.setItem(item);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.countByBooker_IdAndStatusAndEndBefore(anyLong(), any(BookingStatus.class), any(LocalDateTime.class)))
                .thenReturn(1L);
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CommentDto result = itemService.createComment(inputDto, 1L, 1L);

        assertNotNull(result);
        assertEquals("Test comment", result.getText());
        verify(userRepository).findById(1L);
        verify(itemRepository).findById(1L);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void createComment_shouldThrowWhenUserHasNoBookings() {
        CommentDto inputDto = CommentDto.builder().build();
        inputDto.setText("Test comment");

        User author = new User();
        author.setId(1L);

        Item item = new Item();
        item.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.countByBooker_IdAndStatusAndEndBefore(anyLong(), any(BookingStatus.class), any(LocalDateTime.class)))
                .thenReturn(0L);

        assertThrows(NotAvailableItemException.class, () -> itemService.createComment(inputDto, 1L, 1L));
    }
}
