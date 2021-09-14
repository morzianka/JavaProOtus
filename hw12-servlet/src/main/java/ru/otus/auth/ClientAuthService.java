package ru.otus.auth;

public interface ClientAuthService {
    boolean authenticate(String login, String password);
}
