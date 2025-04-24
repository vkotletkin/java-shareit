package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment mapToModel(CommentDto commentDto, Item item, User user) {
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setUser(user);
        comment.setCreated(LocalDateTime.now());
        comment.setText(commentDto.getText());
        return comment;
    }

    public static CommentDto mapToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .created(comment.getCreated())
                .build();
    }
}
