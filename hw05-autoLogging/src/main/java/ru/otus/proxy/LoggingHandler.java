package ru.otus.proxy;

import ru.otus.Log;
import ru.otus.TestLogging;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class LoggingHandler implements InvocationHandler {
    private final static Logger log = Logger.getLogger("LoggerFromProxy");

    private final TestLogging proxiedClass;
    private final Set<MethodInfo> loggingMethods = new HashSet<>();

    LoggingHandler(Class<? extends TestLogging> clazz) throws Exception {
        Constructor<? extends TestLogging> constructor = clazz.getConstructor();
        this.proxiedClass = constructor.newInstance();

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(Log.class)) {
                loggingMethods.add(new MethodInfo(declaredMethod.getName(), declaredMethod.getParameterTypes()));
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {

        MethodInfo methodInfo = new MethodInfo(method.getName(), method.getParameterTypes());
        if (loggingMethods.contains(methodInfo)) {
            logging(methodInfo, args);
        }
        return method.invoke(proxiedClass, args);
    }

    private void logging(MethodInfo methodInfo, Object[] args) {
        log.info(String.format("Method: %s, Params: %s%n", methodInfo.getMethodName(),
                Optional.ofNullable(args).map(arg -> Stream.of(arg).collect(Collectors.toList())).orElse(Collections.emptyList())));
    }
}
