import annotations.After;
import annotations.Before;
import annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Initializer.classInitializer("CustomTestsMain");
    }
}

class Initializer {

    public static void classInitializer(String className) {
        Map<String, List<Method>> methodsMap = new HashMap<>();
        methodsMap.put("Before", new ArrayList<>());
        methodsMap.put("Test", new ArrayList<>());
        methodsMap.put("After", new ArrayList<>());
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
            var clazzInstance = clazz.getConstructor().newInstance();
            Method[] methods;
            methods = clazz.getMethods();
            Arrays.stream(methods).forEach(method -> {
                if (method.isAnnotationPresent(Before.class)) {
                    methodsMap.get("Before").add(method);
                } else if (method.isAnnotationPresent(Test.class)) {
                    methodsMap.get("Test").add(method);
                } else if (method.isAnnotationPresent(After.class)) {
                    methodsMap.get("After").add(method);
                }
            });




            for (Map.Entry<String, List<Method>> entry : methodsMap.entrySet()) {
                entry.getValue().forEach(method -> {
                    try {
                        method.invoke(clazzInstance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        System.out.println("adf");
                    }
                });
            }

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            System.out.printf("Class %s not found%n", className);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(")");
    }
}

