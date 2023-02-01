package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final String selectAll;
    private final String selectById;
    private final String insert;
    private final String update;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaDataClient) {
        String tableName = entityClassMetaDataClient.getName().toLowerCase(Locale.ROOT);
        String columnsName = entityClassMetaDataClient.getAllFields().stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        String columnsWithoutId = entityClassMetaDataClient.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        String insertMarks = Stream.generate(() -> "?")
                .limit(entityClassMetaDataClient.getFieldsWithoutId().size())
                .collect(Collectors.joining(", "));
        String updateMarks = entityClassMetaDataClient.getFieldsWithoutId().stream()
                .map(field -> "set " + field.getName() + " = ?")
                .collect(Collectors.joining(", "));

        this.selectAll = String.format("select %s from %s", columnsName, tableName);
        this.selectById = String.format("select %s from %s where %s  = ?", columnsName, tableName, entityClassMetaDataClient.getIdField().getName());
        this.insert = String.format("insert into %s(%s) values (%s)", tableName, columnsWithoutId, insertMarks);
        this.update = String.format("update %s %s where %s = ?", tableName, updateMarks, entityClassMetaDataClient.getIdField().getName());
    }

    @Override
    public String getSelectAllSql() {
        return this.selectAll;
    }

    @Override
    public String getSelectByIdSql() {
        return this.selectById;
    }

    @Override
    public String getInsertSql() {
        return this.insert;
    }

    @Override
    public String getUpdateSql() {
        return this.update;
    }
}
