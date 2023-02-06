package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jdbc.crm.model.annotation.Id;
import ru.otus.jdbc.crm.model.exception.ConstructorNotFound;
import ru.otus.jdbc.crm.model.exception.NoIdFieldException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private static final Logger log = LoggerFactory.getLogger(EntityClassMetaDataImpl.class);

    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.name = clazz.getSimpleName();

        this.allFields = List.of(clazz.getDeclaredFields());

        this.idField = allFields.stream()
                .filter(field -> field.getAnnotation(Id.class) != null)
                .findAny().orElseThrow(() -> new NoIdFieldException(String.format("Class %s don't contain any field with '@Id' annotation", name)));

        this.fieldsWithoutId = new ArrayList<>(allFields);
        fieldsWithoutId.remove(idField);

        Class<?>[] parameterTypes = new Class<?>[allFields.size()];
        for (int i = 0; i < allFields.size(); i++) {
            var a = allFields.get(i).getType();
            parameterTypes[i] = a;
        }
        try {
            this.constructor = clazz.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            String messageError = String.format("Class %s don't has public constructor for this types", name);
            log.error(messageError);
            throw new ConstructorNotFound(messageError);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
