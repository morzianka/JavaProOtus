package ru.otus.processor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessorExceptionableTest {

    private static final long ID = 1;
    private static final int EVEN_SECOND = 2;
    private static final int ODD_SECOND = 3;

    @Test
    void process() {
        Message message = new Message.Builder(ID).build();
        Clock clock = Clock.fixed(LocalDateTime.now().withSecond(EVEN_SECOND).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        Processor processor = new ProcessorExceptionable(clock);
        assertThrows(RuntimeException.class, () -> processor.process(message));
    }

    @Test
    void processError() {
        Message message = new Message.Builder(ID).build();
        Clock clock = Clock.fixed(LocalDateTime.now().withSecond(ODD_SECOND).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        Processor processor = new ProcessorExceptionable(clock);
        assertDoesNotThrow(() -> processor.process(message));
    }
}