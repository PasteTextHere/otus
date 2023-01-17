package ru.otus.proxy;

import java.util.Arrays;
import java.util.Objects;

final class MethodInfo {
    private final String name;
    private final Class<?>[] parameters;

    MethodInfo(String name, Class<?>[] parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getMethodName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodInfo that = (MethodInfo) o;
        return Objects.equals(name, that.name) && Arrays.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }
}
