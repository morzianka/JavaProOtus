package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messages = new ConcurrentHashMap<>();

    @Override
    public void onUpdated(Message msg) {
        messages.putIfAbsent(msg.getId(), msg.toBuilder().build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messages.get(id));
    }
}
