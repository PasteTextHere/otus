package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jdbc.core.repository.executor.DbExecutorImpl;
import ru.otus.jdbc.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.jdbc.crm.datasource.DriverManagerDataSource;
import ru.otus.jdbc.crm.model.Manager;
import ru.otus.jdbc.crm.service.DbServiceManagerImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;

public class HomeWork {
    private static final String URL = "jdbc:postgresql://127.0.0.1:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {

        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<>(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);

        long cachedClientId = dbServiceManager.saveManager(new Manager("withCache")).getNo();

        long startTime;
        long finishTime;

        startTime = System.nanoTime();
        dbServiceManager.getManager(55);
        finishTime = System.nanoTime();
        long withoutCacheTime = finishTime - startTime;

        startTime = System.nanoTime();
        dbServiceManager.getManager(cachedClientId);
        finishTime = System.nanoTime();
        long withCacheTime = finishTime - startTime;

        log.warn("\nCache time:         {}," +
                 "\nWithout cache time  {}", withCacheTime, withoutCacheTime);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
