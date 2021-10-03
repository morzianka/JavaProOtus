package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Client;
import ru.otus.repo.ClientRepository;

/**
 * @author Shabunina Anita
 */
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    @Override
    @Transactional
    public Iterable<Client> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void add(Client client) {
        repository.save(client);
    }
}
