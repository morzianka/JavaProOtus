package ru.otus.session.manager;

public interface TransactionManager {

    <T> T doInTransaction(TransactionAction<T> action);
}
