package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.repository.ItemService;

import java.util.Collection;

/**
 * Контроллер для управления вещами.
 * Обрабатывает запросы, связанные с CRUD-операциями вещей и комментариев.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private static final String HEADER_SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    /**
     * Создание новой вещи.
     *
     * @param dto    DTO вещи.
     * @param userId ID пользователя.
     * @return Созданный объект вещи.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@Valid @RequestBody ItemDto dto, @RequestHeader(HEADER_SHARER_USER_ID) Long userId) {
        log.info("Создание новой вещи: {} для пользователя с ID = {}", dto.getName(), userId);
        return itemService.createItem(userId, dto);
    }

    /**
     * Обновление вещи.
     *
     * @param itemId ID вещи.
     * @param dto    DTO с обновлением.
     * @param userId ID пользователя.
     * @return Обновленная вещь.
     */
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId, @RequestBody UpdateItemDto dto,
                              @RequestHeader(HEADER_SHARER_USER_ID) Long userId) {
        log.info("Обновление вещи с ID = {} пользователем с ID = {}", itemId, userId);
        return itemService.updateItem(itemId, dto, userId);
    }

    /**
     * Получение вещи по ID.
     *
     * @param itemId ID вещи.
     * @param userId ID пользователя.
     * @return Расширенная информация о вещи.
     */
    @GetMapping("/{itemId}")
    public ExtendedItemDto getItemById(@PathVariable Long itemId,
                                       @RequestHeader(HEADER_SHARER_USER_ID) Long userId) {
        log.info("Запрос вещи с ID = {} пользователем с ID = {}", itemId, userId);
        return itemService.getItemById(itemId);
    }

    /**
     * Получение всех вещей пользователя.
     *
     * @param userId ID пользователя.
     * @return Список вещей пользователя.
     */
    @GetMapping
    public Collection<ItemDto> getAllItemsByOwner(@RequestHeader(HEADER_SHARER_USER_ID) Long userId) {
        log.info("Запрос списка вещей пользователя с ID = {}", userId);
        return itemService.getAllItemsByOwner(userId);
    }

    /**
     * Поиск вещей по тексту.
     *
     * @param text   Текст для поиска.
     * @param userId ID пользователя.
     * @return Список найденных вещей.
     */
    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text,
                                           @RequestHeader(HEADER_SHARER_USER_ID) Long userId) {
        log.info("Поиск вещей для пользователя с ID = {} с текстом '{}'", userId, text);
        return itemService.searchItems(text);
    }

    /**
     * Добавление комментария к вещи.
     *
     * @param itemId   ID вещи.
     * @param authorId ID автора.
     * @param dto      DTO комментария.
     * @return Созданный комментарий.
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentToItem(@PathVariable long itemId,
                                       @RequestHeader(name = HEADER_SHARER_USER_ID) long authorId,
                                       @RequestBody @Valid CommentDto dto) {
        log.info("Добавление комментария к вещи с ID = {} пользователем с ID = {}", itemId, authorId);
        return itemService.addCommentToItem(authorId, itemId, dto);
    }
}