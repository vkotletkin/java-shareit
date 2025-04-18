package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputRequest;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Rollback
@Transactional
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceIntegrationTests {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingService bookingService;

    private User user;
    private Item item;
    private BookingInputRequest bookingInputRequest;


    @BeforeEach
    public void setUp() {

        user = User.builder()
                .name("Vladislav")
                .email("vkotletkin@gmail.com")
                .build();

        item = Item.builder()
                .name("Test item name")
                .description("Test item description")
                .available(true)
                .owner(user)
                .requestId(1L)
                .build();

        bookingInputRequest = BookingInputRequest.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    public void testSaveBookingInputRequest() {
        user = userRepository.save(user);
        item = itemRepository.save(item);

        bookingInputRequest.setBookerId(user.getId());
        bookingInputRequest.setItemId(item.getId());

        BookingDto bookingDto = bookingService.create(bookingInputRequest);

        assertThat(bookingDto.getId(), notNullValue());
        assertThat(bookingDto.getStart(), equalTo(bookingInputRequest.getStart()));
        assertThat(bookingDto.getEnd(), equalTo(bookingInputRequest.getEnd()));
        assertThat(bookingDto.getBooker().getId(), equalTo(bookingInputRequest.getBookerId()));
    }

    @Test
    public void testGetSpecificBooking() {
        user = userRepository.save(user);
        item = itemRepository.save(item);

        bookingInputRequest.setBookerId(user.getId());
        bookingInputRequest.setItemId(item.getId());

        BookingDto bookingDto = bookingService.create(bookingInputRequest);

        BookingDto bookingSpecificDto = bookingService.specificBooking(bookingDto.getId(), user.getId());

        assertThat(bookingSpecificDto.getId(), notNullValue());
        assertThat(bookingSpecificDto.getStart(), equalTo(bookingInputRequest.getStart()));
        assertThat(bookingSpecificDto.getEnd(), equalTo(bookingInputRequest.getEnd()));
        assertThat(bookingSpecificDto.getBooker().getId(), equalTo(user.getId()));
    }
}
