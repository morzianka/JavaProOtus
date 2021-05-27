package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

/**
 * @author Shabunina Anita
 */
public class ProcessorExceptionable implements Processor {

    @Override
    public Message process(Message message) {
        int second = LocalDateTime.parse(message.getField11()).getSecond();
        if (second % 2 == 0) {
            throw new RuntimeException("Чётная секунда!");
        }
        return message;
    }
}
