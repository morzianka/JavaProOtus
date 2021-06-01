package ru.otus.processor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessorExceptionableTest {

    private static final long ID = 1;

    private final Processor processor = new ProcessorExceptionable();

    @Test
    void process() {
        LocalDateTime now = now();
        int second = now.getSecond();
        Message message = new Message.Builder(ID)
                .field11(now().toString())
                .build();
        //code smell - no reason for runtime LocalDateTime.now()
        if (second % 2 == 0) {
            assertThrows(RuntimeException.class, () -> processor.process(message));
        } else {
            assertDoesNotThrow(() -> processor.process(message));
        }
    }
}