package example.concurrency.synchronization.lock.counters.counter;

import example.concurrency.synchronization.lock.counters.Counter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * AtomicReference = volatile + unsafe/(AtomicLongFieldUpdater) CAS
 */
public class Atomic implements Counter {
    private final AtomicLong atomic = new AtomicLong();

    public long getCount() {
        return atomic.get();
    }

    public void increment() {
        atomic.incrementAndGet();
    }
}
