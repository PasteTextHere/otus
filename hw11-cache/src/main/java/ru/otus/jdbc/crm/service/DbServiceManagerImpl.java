package ru.otus.jdbc.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.jdbc.core.repository.DataTemplate;
import ru.otus.jdbc.core.sessionmanager.TransactionRunner;
import ru.otus.jdbc.crm.model.Manager;

import java.util.List;
import java.util.Optional;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<Long, Manager> cache;

    public DbServiceManagerImpl(TransactionRunner transactionRunner, DataTemplate<Manager> managerDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
        this.cache = new MyCache<>();
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                putInCache(createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            putInCache(manager);
            log.info("updated manager: {}", manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        Optional<Manager> manager = Optional.ofNullable(cache.get(no));
        if (manager.isEmpty()) {
            manager = transactionRunner.doInTransaction(connection -> {
                Optional<Manager> managerOptional = managerDataTemplate.findById(connection, no);
                log.info("manager: {}", managerOptional);
                managerOptional.ifPresent(value -> cache.put(value.getNo(), managerOptional.get()));
                return managerOptional;
            });
        }
        return manager;
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
        });
    }

    private void putInCache(Manager manager) {
        cache.put(manager.getNo(), manager);
    }
}
