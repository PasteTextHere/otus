package ru.otus.processor;

import ru.otus.model.Message;

public class LoggerProcessor implements Processor {

    private final Processor processor;

    public LoggerProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public Message process(Message message) {
        System.out.printf("log processing message: \n%s\n",  message);
        return processor.process(message);
    }
}
