package example.concurrency.synchronization.lock.counters.counter;

import example.concurrency.synchronization.lock.counters.Counter;

public class Synchronized implements Counter {
    private final Object lock = new Object();

    private int counter;

    public long getCount() {
        synchronized (lock) {
            return counter;
        }
    }

    public void increment() {
        synchronized (lock) {
            ++counter;
        }
    }
}
