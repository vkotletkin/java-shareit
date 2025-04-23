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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceUnitTests {

    private final User testUser = User.builder()
            .id(1L)
            .name("Test User")
            .email("test@example.com")
            .build();
    private final Item testItem = Item.builder()
            .id(1L)
            .name("Test Item")
            .description("Test Description")
            .available(true)
            .owner(testUser)
            .build();
    private final ItemDto testItemDto = ItemDto.builder()
            .id(1L)
            .name("Test Item")
            .description("Test Description")
            .available(true)
            .ownerId(1L)
            .build();
    private final CommentDto testCommentDto = CommentDto.builder()
            .id(1L)
            .text("Test Comment")
            .authorName("Test User")
            .build();
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
    void findAllItemsByUserShouldReturnItemsWhenUserExists() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(itemRepository.findByOwnerIdEquals(anyLong())).thenReturn(List.of(testItem));

        Collection<ItemDto> result = itemService.findAllItemsByUser(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).findByOwnerIdEquals(anyLong());
    }

    @Test
    void findAllItemsByUserShouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> itemService.findAllItemsByUser(1L));
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, never()).findByOwnerIdEquals(anyLong());
    }

    @Test
    void findByIdShouldReturnItemWithoutBookingsWhenNoBookingsExist() {

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(testItem));
        when(bookingRepository.findAllByItem_Id(anyLong())).thenReturn(Collections.emptyList());
        when(commentRepository.findByItem_Id(anyLong())).thenReturn(Collections.emptyList());

        ItemEnrichedDto result = itemService.findById(1L);

        assertNotNull(result);
        assertEquals(testItem.getId(), result.getId());
        assertNull(result.getLastBooking());
        assertNull(result.getNextBooking());
        assertTrue(result.getComments().isEmpty());
        verify(itemRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).findAllByItem_Id(anyLong());
        verify(commentRepository, times(1)).findByItem_Id(anyLong());
    }

    @Test
    void findByIdShouldReturnItemWithBookingsWhenBookingsExist() {

        LocalDateTime now = LocalDateTime.now();

        Booking currentBooking = Booking.builder()
                .id(1L)
                .start(now.minusHours(1))
                .end(now.plusHours(1))
                .status(BookingStatus.APPROVED)
                .item(testItem)
                .booker(testUser)
                .build();

        Booking futureBooking = Booking.builder()
                .id(2L)
                .start(now.plusHours(2))
                .end(now.plusHours(3))
                .status(BookingStatus.APPROVED)
                .item(testItem)
                .booker(testUser)
                .build();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(testItem));
        when(bookingRepository.findAllByItem_Id(anyLong())).thenReturn(List.of(currentBooking, futureBooking));
        when(commentRepository.findByItem_Id(anyLong())).thenReturn(Collections.emptyList());

        ItemEnrichedDto result = itemService.findById(1L);

        assertNotNull(result);
        assertEquals(testItem.getId(), result.getId());

        assertNotNull(result.getLastBooking(), "Last booking should not be null");
        assertEquals(currentBooking.getEnd(), result.getLastBooking());

        assertNotNull(result.getNextBooking(), "Next booking should not be null");
        assertEquals(futureBooking.getStart(), result.getNextBooking());

        verify(itemRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).findAllByItem_Id(anyLong());
        verify(commentRepository, times(1)).findByItem_Id(anyLong());
    }

    @Test
    void findByIdShouldThrowExceptionWhenItemNotFound() {

        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> itemService.findById(1L));
        verify(itemRepository, times(1)).findById(anyLong());
        verify(bookingRepository, never()).findAllByItem_Id(anyLong());
        verify(commentRepository, never()).findByItem_Id(anyLong());
    }

    @Test
    void findByTextShouldReturnEmptyListWhenTextIsBlank() {

        Collection<ItemDto> result = itemService.findByText(" ");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(itemRepository, never()).findTextNameAndDescription(anyString());
    }

    @Test
    void findByTextShouldReturnItemsWhenTextIsNotBlank() {

        when(itemRepository.findTextNameAndDescription(anyString())).thenReturn(List.of(testItem));

        Collection<ItemDto> result = itemService.findByText("test");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(itemRepository, times(1)).findTextNameAndDescription(anyString());
    }


    @Test
    void createCommentShouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> itemService.createComment(testCommentDto, 1L, 1L));
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, never()).findById(anyLong());
        verify(bookingRepository, never())
                .countByBooker_IdAndStatusAndEndBefore(anyLong(), any(), any());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void createCommentShouldThrowExceptionWhenItemNotFound() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> itemService.createComment(testCommentDto, 1L, 1L));
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).findById(anyLong());
        verify(bookingRepository, never())
                .countByBooker_IdAndStatusAndEndBefore(anyLong(), any(), any());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void createComment_shouldThrowExceptionWhenNoBookingsExist() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(testItem));
        when(bookingRepository.countByBooker_IdAndStatusAndEndBefore(anyLong(), any(), any()))
                .thenReturn(0L);

        assertThrows(NotAvailableItemException.class,
                () -> itemService.createComment(testCommentDto, 1L, 1L));
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1))
                .countByBooker_IdAndStatusAndEndBefore(anyLong(), any(), any());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void createComment_shouldCreateCommentWhenValid() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(testItem));
        when(bookingRepository.countByBooker_IdAndStatusAndEndBefore(anyLong(), any(), any()))
                .thenReturn(1L);
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            comment.setId(1L);
            return comment;
        });

        CommentDto result = itemService.createComment(testCommentDto, 1L, 1L);

        assertNotNull(result);
        assertEquals(testCommentDto.getText(), result.getText());
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1))
                .countByBooker_IdAndStatusAndEndBefore(anyLong(), any(), any());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void findById_shouldReturnItemWithComments() {

        Comment testComment = Comment.builder()
                .id(1L)
                .text("Test Comment")
                .item(testItem)
                .user(testUser)
                .created(LocalDateTime.now())
                .build();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(testItem));
        when(bookingRepository.findAllByItem_Id(anyLong())).thenReturn(Collections.emptyList());
        when(commentRepository.findByItem_Id(anyLong())).thenReturn(List.of(testComment));

        ItemEnrichedDto result = itemService.findById(1L);

        assertNotNull(result);
        assertFalse(result.getComments().isEmpty());
        assertEquals(testComment.getText(), result.getComments().get(0));
        verify(itemRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).findAllByItem_Id(anyLong());
        verify(commentRepository, times(1)).findByItem_Id(anyLong());
    }
}