package ru.otus.listener.homework;

import ru.otus.model.Message;

import java.util.Optional;

/**
 * @author Shabunina Anita
 */
public interface MessageRepository {

    void add(Message msg);

    Optional<Message> findById(long id);
}
