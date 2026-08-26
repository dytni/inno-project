package by.dytni.commonevents.dto;

import java.time.LocalDate;

public record UserCreatedEvent(
        Long authUserId,
        String email,
        String firstName,
        String lastName,
        LocalDate birthDate
) {}
