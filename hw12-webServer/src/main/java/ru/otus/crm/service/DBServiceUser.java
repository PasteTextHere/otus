package ru.otus.crm.service;

import ru.otus.crm.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {
    User saveUser(User client);

    Optional<User> getUser(long id);

    List<User> findAll();
}
