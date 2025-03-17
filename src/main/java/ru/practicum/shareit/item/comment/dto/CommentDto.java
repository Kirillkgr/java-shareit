package ru.practicum.shareit.item.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;

@Builder (toBuilder = true)
@Data
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentDto {

    long id;

    @NotBlank (message = "Содержимое комментария не может быть пустым")
    @Length (max = 512, message = "Содержимое комментария не может превышать 512 символов.")
    String text;

    ItemDto item;

    String authorName;

    LocalDateTime created;

}
