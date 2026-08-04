package by.dytni.commonevents.dto;

public record UserStatusChangedEvent(
        Long userId,
        String email,
        boolean active
) {}