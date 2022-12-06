package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

public class EvenSecondExceptionProcessor implements Processor {

    @Override
    public Message process(Message message) {
        var startTime = LocalDateTime.now().getSecond();
        if (startTime % 2 == 0) {
            throw new EvenSecondException("Bad luck, call from even second");
        } else {
            return message;
        }
    }
}
