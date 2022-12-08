package ru.otus.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.EvenSecondException;
import ru.otus.processor.homework.EvenSecondExceptionProcessor;

import java.time.LocalDateTime;

class EvenSecondExceptionProcessorTest {

    private EvenSecondExceptionProcessor processor;
    private Message message;
    @BeforeEach
    public void setUp() {
        processor = new EvenSecondExceptionProcessor();
        message = new Message.Builder(123L)
                .field1("Field1Test")
                .build();
    }

    @Test
    @DisplayName("Тестируем выброс исключения на четной секунде вызова")
    void throwException() throws InterruptedException {
        var currentSecond = LocalDateTime.now().getSecond();
        if (currentSecond % 2 != 0) {
            Thread.sleep(1000);
        }

        Assertions.assertThrows(EvenSecondException.class, () -> processor.process(message));
    }

    @Test
    @DisplayName("Тестируем возврат сообщения на нечетной секунде")
    void returnMessage() throws InterruptedException {
        var currentSecond = LocalDateTime.now().getSecond();
        if (currentSecond % 2 == 0) {
            Thread.sleep(1000);
        }
        var returnedMessage = processor.process(message);

        Assertions.assertNotNull(returnedMessage);
        Assertions.assertEquals(message, returnedMessage);
    }
}