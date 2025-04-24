package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "items", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String description;

    @Column(name = "available", nullable = false)
    Boolean available;

    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    User owner;

    @Column(name = "request_id")
    Long requestId;
}
