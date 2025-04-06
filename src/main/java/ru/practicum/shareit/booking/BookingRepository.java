package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_IdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByBooker_Id(long userId, Sort sort);

    List<Booking> findByBooker_IdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime startBefore, LocalDateTime endAfter, Sort sort);

    List<Booking> findByBooker_IdAndEndBefore(long bookerId, LocalDateTime endBefore, Sort sort);

    List<Booking> findByBooker_IdAndStartAfter(long userId, LocalDateTime nowTimestamp, Sort sort);

    List<Booking> findByItem_Owner_Id(long userId, Sort sortByStart);

    List<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfter(long userId, LocalDateTime nowTimestamp, LocalDateTime nowTimestamp1, Sort sortByStart);

    List<Booking> findByItem_Owner_IdAndEndBefore(long userId, LocalDateTime nowTimestamp, Sort sortByStart);

    List<Booking> findByItem_Owner_IdAndStartAfter(long userId, LocalDateTime nowTimestamp, Sort sortByStart);

    List<Booking> findByItem_Owner_IdAndStatus(long userId, BookingStatus bookingStatus, Sort sortByStart);
}
