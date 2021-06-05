package ru.otus.listener.homework;

import ru.otus.model.Message;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Shabunina Anita
 */
public class MessageRepositoryImpl implements MessageRepository {

    private final Map<Long, Message> messages = new ConcurrentHashMap<>();

    @Override
    public void add(Message msg) {
        messages.putIfAbsent(msg.getId(), msg);
    }

    @Override
    public Optional<Message> findById(long id) {
        return Optional.ofNullable(messages.get(id));
    }
}
