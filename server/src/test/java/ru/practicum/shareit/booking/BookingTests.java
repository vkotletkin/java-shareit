package ru.practicum.shareit.booking;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingTests {


    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testBookingEntityMapping() {
        // Given
        User booker = new User();
        booker.setName("Test User");
        booker.setEmail("test@email.com");
        entityManager.persist(booker);

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(booker);
        entityManager.persist(item);

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        // When
        Booking savedBooking = entityManager.persistFlushFind(booking);

        // Then
        assertThat(savedBooking.getId()).isNotNull();
        assertThat(savedBooking.getStart()).isNotNull();
        assertThat(savedBooking.getEnd()).isNotNull();
        assertThat(savedBooking.getItem()).isEqualTo(item);
        assertThat(savedBooking.getBooker()).isEqualTo(booker);
        assertThat(savedBooking.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void testLombokAnnotations() {
        // Given
        Booking booking = new Booking();
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        // When
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(BookingStatus.APPROVED);

        // Then
        assertThat(booking.getStart()).isEqualTo(start);
        assertThat(booking.getEnd()).isEqualTo(end);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(booking.toString()).contains(start.toString())
                .contains(end.toString())
                .contains("APPROVED");
    }

    @Test
    void testLazyLoadingForRelations() {
        // Given
        User booker = new User();
        booker.setName("Test User");
        booker.setEmail("test@email.com");
        entityManager.persist(booker);

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(booker);
        entityManager.persist(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        Booking savedBooking = entityManager.persistFlushFind(booking);

        // When
        entityManager.detach(savedBooking);
        Booking foundBooking = entityManager.find(Booking.class, savedBooking.getId());

        // Then
        assertThat(foundBooking.getItem().getName()).isEqualTo("Test Item");
        assertThat(foundBooking.getBooker().getName()).isEqualTo("Test User");
    }
}
