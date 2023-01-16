package ru.otus.proxy;

import ru.otus.TestLogging;

import java.lang.reflect.Proxy;

public class ProxyForLogging {

    private ProxyForLogging() {
    }

    public static TestLogging createProxyClass(Class<? extends TestLogging> clazz) throws Exception {
        return (TestLogging) Proxy.newProxyInstance(ProxyForLogging.class.getClassLoader(),
                new Class<?>[]{TestLogging.class},
                new LoggingHandler(clazz));
    }
}

