package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"item", "booker"})
    List<Booking> findByBooker_IdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"item", "booker"})
    List<Booking> findByBooker_Id(long userId, Sort sort);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"item", "booker"})
    List<Booking> findByBooker_IdAndStartBeforeAndEndAfter
            (Long bookerId, LocalDateTime startBefore, LocalDateTime endAfter, Sort sort);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"item", "booker"})
    List<Booking> findByBooker_IdAndEndBefore(long bookerId, LocalDateTime endBefore, Sort sort);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"item", "booker"})
    List<Booking> findByBooker_IdAndStartAfter(long userId, LocalDateTime nowTimestamp, Sort sort);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"item", "booker"})
    List<Booking> findByItem_Owner_Id(long userId, Sort sortByStart);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"item", "booker"})
    List<Booking> findByItem_Owner_Id(long userId);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"item", "booker"})
    List<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfter
            (long userId, LocalDateTime nowTimestamp, LocalDateTime nowTimestamp1, Sort sortByStart);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"item", "booker"})
    List<Booking> findByItem_Owner_IdAndEndBefore(long userId, LocalDateTime nowTimestamp, Sort sortByStart);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"item", "booker"})
    List<Booking> findByItem_Owner_IdAndStartAfter(long userId, LocalDateTime nowTimestamp, Sort sortByStart);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"item", "booker"})
    List<Booking> findByItem_Owner_IdAndStatus(long userId, BookingStatus bookingStatus, Sort sortByStart);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"item", "booker"})
    List<Booking> findAllByItem_Id(long id);
}
