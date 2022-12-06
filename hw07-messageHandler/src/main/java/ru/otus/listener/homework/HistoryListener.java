package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private Deque<Message> history = new ArrayDeque<>();

    @Override
    public void onUpdated(Message msg) {
        var field13Data = new ObjectForMessage();
        field13Data.setData(msg.getField13().getData().stream().toList());
        var a = new Message.Builder(msg.getId())
                .field1(msg.getField1())
                .field2(msg.getField2())
                .field3(msg.getField3())
                .field4(msg.getField4())
                .field5(msg.getField5())
                .field6(msg.getField6())
                .field7(msg.getField7())
                .field8(msg.getField8())
                .field9(msg.getField9())
                .field10(msg.getField10())
                .field11(msg.getField11())
                .field12(msg.getField12())
                .field13(field13Data)
                .build();
        history.add(a);
//        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        var currentMessage = new Message.Builder(id).build();
        return history.stream().filter(msg -> msg.equals(currentMessage)).findFirst();

//        throw new UnsupportedOperationException();
    }
}
