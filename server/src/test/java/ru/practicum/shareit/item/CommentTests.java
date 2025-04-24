package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentTests {

    @Test
    void commentShouldHaveCorrectGettersAndSetters() {

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");

        User author = new User();
        author.setId(1L);
        comment.setUser(author);

        Item item = new Item();
        item.setId(1L);
        comment.setItem(item);

        LocalDateTime now = LocalDateTime.now();
        comment.setCreated(now);

        assertEquals(1L, comment.getId());
        assertEquals("Test comment", comment.getText());
        assertEquals(author, comment.getUser());
        assertEquals(item, comment.getItem());
        assertEquals(now, comment.getCreated());
    }
}
