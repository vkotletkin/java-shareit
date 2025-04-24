package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestor_Id(long userId);

    @Query("""
            SELECT itr FROM ItemRequest itr
            WHERE itr.requestor.id != ?1
            """)
    List<ItemRequest> findByRequestorIdNotEqual(long userId);
}
