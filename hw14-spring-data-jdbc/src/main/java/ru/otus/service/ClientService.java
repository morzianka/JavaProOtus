package ru.otus.service;

import ru.otus.model.Client;

/**
 * @author Shabunina Anita
 */
public interface ClientService {

    Iterable<Client> getAll();

    void add(Client client);
}
