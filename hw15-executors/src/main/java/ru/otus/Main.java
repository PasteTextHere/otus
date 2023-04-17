package ru.otus;

import java.util.List;

public class Main {

    static List<Integer> numbersList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    private static final String firstThreadName = "Thread 1";
    private static final String secondThreadName = "Thread 2";
    private String last = secondThreadName;


    public static void main(String[] args) {
        Main mainClass = new Main();
        Thread first = new Thread(mainClass::worker, firstThreadName);
        Thread second = new Thread(mainClass::worker, secondThreadName);
        first.start();
        second.start();
    }

    private synchronized void worker() {
        int position = -1;
        boolean reverse = false;
        while (true) {
            try {
                if (reverse) {
                    position--;
                }
                else {
                    position++;
                }

                numberPrinter(position);
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted exception");
            }
            if (position == 9) {
                reverse = true;
            } else if (position == 0) {
                reverse = false;
            }
        }
    }

    private void numberPrinter(int i) throws InterruptedException {
        while (last.equals(Thread.currentThread().getName())) {
            wait();
        }
        System.out.printf("%s: %s\n", Thread.currentThread().getName(), numbersList.get(i));
        last = Thread.currentThread().getName();
        Thread.sleep(100);
        notifyAll();
    }

}
