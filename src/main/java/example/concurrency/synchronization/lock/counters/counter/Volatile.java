package example.concurrency.synchronization.lock.counters.counter;

import example.concurrency.synchronization.lock.counters.Counter;

public class Volatile implements Counter {
    private volatile long counter;

    public long getCount() {
        return counter;
    }

    public void increment() {
        // 流弊，idea竟然会提醒：non-atomic operation on volatile value
        ++counter;
    }
}