package example.concurrency.synchronization.lock.counters.counter;

import example.concurrency.synchronization.lock.counters.Counter;

import java.util.concurrent.atomic.LongAdder;

public class Adder implements Counter {
    private final LongAdder adder = new LongAdder();

    public long getCount() {
        return adder.longValue();
    }

    public void increment() {
        adder.increment();
    }
}
