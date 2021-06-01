package ru.otus.processor;

import ru.otus.model.Message;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * @author Shabunina Anita
 */
public class ProcessorExceptionable implements Processor {

    private final Clock clock;

    public ProcessorExceptionable(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Message process(Message message) {
        if (LocalDateTime.now(clock).getSecond() % 2 == 0) {
            throw new RuntimeException("Чётная секунда!");
        }
        return message;
    }
}
