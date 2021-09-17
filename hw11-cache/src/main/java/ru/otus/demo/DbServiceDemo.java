package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.Optional;
import java.util.function.Function;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        // save client
        Client client = dbServiceClient.saveClient(new Client("dbServiceFirst"));

        // select 1000 times, 10 gc calls

        // Time spent without cache: ~3546
        execute(n -> n.getClientFromDb(client.getId()), dbServiceClient);

        // Time spent with cache: ~722
        execute(n -> n.getClient(client.getId()), dbServiceClient);
    }

    private static void execute(Function<DBServiceClient, Optional<Client>> fun, DBServiceClient dbServiceClient) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            if (i % 100 == 0) {
                System.gc();
            }
            fun.apply(dbServiceClient);
        }
        long stop = System.currentTimeMillis();
        log.info("Time spent: {}", stop - start);
    }
}
