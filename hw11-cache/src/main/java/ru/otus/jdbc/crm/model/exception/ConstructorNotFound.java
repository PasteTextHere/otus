package ru.otus.jdbc.crm.model.exception;

public class ConstructorNotFound extends RuntimeException {
    public ConstructorNotFound(String messageError) {
        super(messageError);
    }

    public ConstructorNotFound(ReflectiveOperationException e) {
        super(e);
    }
}
