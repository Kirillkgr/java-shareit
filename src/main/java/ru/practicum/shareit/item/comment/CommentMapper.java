package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    /**
     * Преобразует объект CommentDto в сущность Comment.
     *
     * @param authorId идентификатор автора.
     * @param itemId   идентификатор вещи.
     * @param dto      объект DTO с данными комментария.
     * @return созданная сущность Comment.
     */
    public static Comment toComment(long authorId, long itemId, CommentDto dto) {
        if (dto == null || dto.getText() == null || dto.getText().isBlank()) {
            throw new IllegalArgumentException("Текст комментария не может быть пустым");
        }

        return Comment.builder()
                .text(dto.getText())
                .item(Item.builder().id(itemId).build())
                .author(User.builder().id(authorId).build())
                .createDate(LocalDateTime.now())
                .build();
    }

    /**
     * Преобразует сущность Comment в объект CommentDto.
     *
     * @param comment объект сущности Comment.
     * @return объект DTO с данными комментария.
     */
    public static CommentDto toCommentDto(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Комментарий не может быть null");
        }

        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(ItemMapper.mapToItemDto(comment.getItem()))
                .authorName(comment.getAuthor() != null ? comment.getAuthor().getName() : null)
                .created(comment.getCreateDate())
                .build();
    }
}