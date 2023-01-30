package ru.otus.jdbc.mapper;

import ru.otus.jdbc.core.repository.DataTemplate;
import ru.otus.jdbc.core.repository.DataTemplateException;
import ru.otus.jdbc.core.repository.executor.DbExecutor;
import ru.otus.jdbc.crm.model.exception.ConstructorNotFound;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createObjectFromResultSet(rs);
                }
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
            return null;
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), Collections.emptyList(), rs -> {
            List<T> entityList = new ArrayList<>();
            try {
                while (rs.next()) {
                    entityList.add(createObjectFromResultSet(rs));
                }
                return entityList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElse(Collections.emptyList());
    }

    @Override
    public long insert(Connection connection, T entity) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getFieldsParamWithoutId(entity));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getAllFieldsParam(entity));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T createObjectFromResultSet(ResultSet rs) throws SQLException {
        T entity;
        Object[] args = new Object[entityClassMetaData.getAllFields().size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = rs.getObject(entityClassMetaData.getAllFields().get(i).getName());
        }
        try {
            entity = entityClassMetaData.getConstructor().newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ConstructorNotFound(e);
        }
        return entity;
    }

    private List<Object> getFieldsParamWithoutId(T entity) throws IllegalAccessException {
        List<Object> list = new ArrayList<>();
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            list.add(field.get(entity));
            field.setAccessible(false);
        }
        return list;
    }

    private List<Object> getAllFieldsParam(T entity) throws IllegalAccessException {
        List<Object> list = new ArrayList<>();
        for (Field field : entityClassMetaData.getAllFields()) {
            field.setAccessible(true);
            list.add(field.get(entity));
            field.setAccessible(false);
        }
        return list;
    }
}
