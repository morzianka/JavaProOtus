package ru.otus.processor;

import ru.otus.model.Message;

/**
 * @author Shabunina Anita
 */
public class ProcessorSwapField11AndField12 implements Processor {

    @Override
    public Message process(Message message) {
        return message.toBuilder()
                .field12(message.getField11())
                .field11(message.getField12())
                .build();
    }
}
