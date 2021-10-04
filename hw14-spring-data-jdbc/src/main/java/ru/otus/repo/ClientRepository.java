package ru.otus.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.model.Client;

/**
 * @author Shabunina Anita
 */
@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
}
