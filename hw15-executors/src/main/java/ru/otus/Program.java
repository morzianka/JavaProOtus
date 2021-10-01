package ru.otus;

import java.util.concurrent.*;

/**
 * @author Shabunina Anita
 */
public class Program {

    private static final Counter counter = new Counter();

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleWithFixedDelay(Program::print, 0, 1, TimeUnit.MILLISECONDS);
        executor.scheduleWithFixedDelay(Program::print, 100, 1, TimeUnit.MILLISECONDS);
    }

    private static void print() {
        synchronized (counter) {
            System.out.printf("%s: %s%n", Thread.currentThread().getName(), counter.getCount());
            try {
                counter.notify();
                counter.wait();
                counter.switchNeedToChange();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
