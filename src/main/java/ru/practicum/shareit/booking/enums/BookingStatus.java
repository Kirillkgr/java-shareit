package ru.practicum.shareit.booking.enums;

public enum BookingStatus {
    WAITING,  // Ожидает подтверждения
    APPROVED, // Подтверждено владельцем
    REJECTED, // Отклонено владельцем
    CANCELED  // Отменено пользователем
}

