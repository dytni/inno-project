package by.dytni.commonevents.dto;

public record UserStatusChangedEvent(
        String email,
        boolean active
) {}