package ru.otus;

import ru.otus.proxy.ProxyForLogging;

public class Main {
    public static void main(String[] args) throws Exception {
        var testLogging = ProxyForLogging.createProxyClass(TestLoggingImpl.class);

        testLogging.calculation(1);
        testLogging.calculation(1,4, "test");
        testLogging.calculation(1,4);
        testLogging.calculation();
        testLogging.calculation(1,4);

    }
}
