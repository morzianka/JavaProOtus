package ru.otus.auth;

import ru.otus.crm.service.DBServiceClient;

public class ClientAuthServiceImpl implements ClientAuthService {

    private final DBServiceClient dbServiceClient;

    public ClientAuthServiceImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dbServiceClient.findByLogin(login)
                .map(client -> client.getPassword().equals(password))
                .orElse(false);
    }

}
