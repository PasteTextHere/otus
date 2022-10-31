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
    private static int successTests = 0;
    private static int failedTests = 0;

    public static void classInitializer(String className) {
        Map<String, List<Method>> methodsMap = new HashMap<>();
        methodsMap.put("Before", new ArrayList<>());
        methodsMap.put("Test", new ArrayList<>());
        methodsMap.put("After", new ArrayList<>());
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
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

            for (Method testMethod : methodsMap.get("Test")) {
                runTestMethod(clazz, methodsMap.get("Before"), testMethod, methodsMap.get("After"));
            }
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            System.out.println("Tests run ruined!!!");
        }
        printTestsResult();
    }

    private static void runTestMethod(Class<?> testingClass,
                                      List<Method> beforeTest,
                                      Method testMethod,
                                      List<Method> afterTest)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var newInstance = testingClass.getConstructor().newInstance();
        boolean beforeSuccess = true;

        for (Method method : beforeTest) {
            try {
                method.setAccessible(true);
                method.invoke(newInstance);
            } catch (Exception e) {
                failedTests++;
                beforeSuccess = false;
                break;
            }
        }

        if (beforeSuccess) {
            try {
                testMethod.setAccessible(true);
                testMethod.invoke(newInstance);
                for (Method method : afterTest) {
                    method.setAccessible(true);
                    method.invoke(newInstance);
                    successTests++;
                }
            } catch (Exception e) {
                failedTests++;
            }
        }


    }

    private static void printTestsResult() {
        System.out.printf(
                "---------------------------------%n" +
                        "Total test passed: %d, %n" +
                        "Total test failed: %d, %n" +
                        "---------------------------------", successTests, failedTests);
    }


}

