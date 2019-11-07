package example.concurrency.synchronization.lock.counters.counter;

import example.concurrency.synchronization.lock.counters.Counter;

public class Dirty implements Counter {
    private long counter;

    public long getCount() {
        return counter;
    }

    public void increment() {
        ++counter;
    }
}
