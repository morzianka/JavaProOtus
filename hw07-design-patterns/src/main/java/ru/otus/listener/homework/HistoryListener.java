package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final MessageRepository messageRepository;

    public HistoryListener(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void onUpdated(Message msg) {
        messageRepository.add(new Message(msg));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return messageRepository.findById(id);
    }
}
