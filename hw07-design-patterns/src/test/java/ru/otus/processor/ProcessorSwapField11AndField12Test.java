package ru.otus.processor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.ProcessorSwapField11AndField12;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessorSwapField11AndField12Test {

    private static final long ID = 1;
    private static final String FIELD_11 = "field11";
    private static final String FIELD_12 = "field12";

    private final Processor processor = new ProcessorSwapField11AndField12();

    @Test
    void process() {
        Message message = new Message.Builder(ID)
                .field11(FIELD_11)
                .field12(FIELD_12)
                .build();

        Message expected = new Message.Builder(ID)
                .field11(FIELD_12)
                .field12(FIELD_11)
                .build();
        Message actual = processor.process(message);

        assertEquals(expected.getField11(), actual.getField11());
        assertEquals(expected.getField12(), actual.getField12());
    }
}