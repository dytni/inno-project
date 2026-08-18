package by.dytni.commonevents;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
public abstract class Consumer<T> {

    protected final ObjectMapper objectMapper;

    protected T getObject(String message) {
        try {
            return objectMapper.readValue(message, getType());
        }
        catch (Exception e) {
            log.error("failed to process message = {}", message, e);
            throw new RuntimeException("failed to process message", e);
        }
    }

    protected abstract Class<T> getType();
}
