package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {
        private final List<Object> appComponents = new ArrayList<>();
        private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        checkConfigClass(configClass);

        Object configClassInstance = configClass.getConstructor().newInstance();

        List<Method> methods = Arrays.stream(configClass.getMethods()).filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order())).toList();

        String componentName;
        Object component;
        for (Method method : methods) {
            componentName = method.getAnnotation(AppComponent.class).name();
            if (appComponentsByName.containsKey(componentName)) {
                throw new RuntimeException("Duplicate component name: " + componentName);
            }
            component = method.invoke(configClassInstance, getArgs(method.getParameters()));

            appComponentsByName.put(componentName, component);
            appComponents.add(component);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

        @Override
        public <C> C getAppComponent(Class<C> componentClass) {
            List<Object> components = appComponents.stream().filter(component -> componentClass.isAssignableFrom(component.getClass())).toList();
            if (components.size() > 1) {
                throw new RuntimeException("Component is more then one instance");
            } else if (components.size() == 0) {
                throw new RuntimeException("Component is absent in container");
            }
            return (C) components.get(0);
        }

        @Override
        public <C> C getAppComponent(String componentName) {
            return (C) Optional.ofNullable(appComponentsByName.get(componentName)).orElseThrow(() -> new RuntimeException("Component is absent in container"));
        }

        private Object[] getArgs(Parameter[] parameters) {
            Object[] args = new Object[parameters.length];
            for (int i = 0; i < args.length; i++) {
                args[i] = getAppComponent(parameters[i].getType());
            }
            return args;
        }
}
