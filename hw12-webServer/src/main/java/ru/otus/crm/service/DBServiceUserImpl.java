package ru.otus.crm.service;

import ru.otus.crm.model.User;

import java.util.List;
import java.util.Optional;

public class DBServiceUserImpl implements DBServiceUser {
    @Override
    public User saveUser(User client) {
        return null;
    }

    @Override
    public Optional<User> getUser(long id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
