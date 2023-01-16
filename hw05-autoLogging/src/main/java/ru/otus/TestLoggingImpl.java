package ru.otus;

public class TestLoggingImpl implements TestLogging {

    @Override
    @Log
    public void calculation() {
    }

    @Override
    @Log
    public void calculation(int param1) {
    }

    @Override
    @Log
    public void calculation(int param1, int param2) {
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
    }
}
