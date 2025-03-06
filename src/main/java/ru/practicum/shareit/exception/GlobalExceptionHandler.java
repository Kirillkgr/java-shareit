package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

/**
 * Глобальный обработчик исключений для управления исключениями приложения.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение BookingException и возвращает ответ с кодом BAD_REQUEST.
     *
     * @param ex исключение BookingException.
     * @return карта с сообщением об ошибке.
     */
    @ExceptionHandler (BookingException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBookingException(BookingException ex) {
        return createErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключение CommentException и возвращает ответ с кодом BAD_REQUEST.
     *
     * @param ex исключение CommentException.
     * @return карта с сообщением об ошибке.
     */
    @ExceptionHandler (CommentException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public Map<String, String> handleCommentException(CommentException ex) {
        return createErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключение AccessDeniedException и возвращает ответ с кодом FORBIDDEN.
     *
     * @param ex исключение AccessDeniedException.
     * @return карта с сообщением об ошибке.
     */
    @ExceptionHandler (AccessDeniedException.class)
    @ResponseStatus (HttpStatus.FORBIDDEN)
    public Map<String, String> handleAccessDeniedException(AccessDeniedException ex) {
        return createErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключение NotFoundException и возвращает ответ с кодом NOT_FOUND.
     *
     * @param ex исключение NotFoundException.
     * @return карта с сообщением об ошибке.
     */
    @ExceptionHandler (NotFoundException.class)
    @ResponseStatus (HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(NotFoundException ex) {
        return createErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключение DuplicateEmailException и возвращает ответ с кодом CONFLICT.
     *
     * @param ex исключение DuplicateEmailException.
     * @return карта с сообщением об ошибке.
     */
    @ExceptionHandler (DuplicateEmailException.class)
    @ResponseStatus (HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicateEmailException(DuplicateEmailException ex) {
        return createErrorResponse(ex.getMessage());
    }

    /**
     * Утилитарный метод для создания ответа с сообщением об ошибке.
     *
     * @param errorMessage сообщение об ошибке.
     * @return карта с сообщением об ошибке.
     */
    private Map<String, String> createErrorResponse(String errorMessage) {
        return Collections.singletonMap("error", errorMessage);
    }
}