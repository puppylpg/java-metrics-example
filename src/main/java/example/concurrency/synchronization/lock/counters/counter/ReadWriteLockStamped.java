package example.concurrency.synchronization.lock.counters.counter;

import example.concurrency.synchronization.lock.counters.Counter;

import java.util.concurrent.locks.StampedLock;

public class ReadWriteLockStamped implements Counter {

    private StampedLock rwlock = new StampedLock();

    private long counter;

    public long getCount() {
        long stamp = rwlock.readLock();

        try {
            return counter;
        } finally {
            rwlock.unlockRead(stamp);
        }
    }

    public void increment() {
        long stamp = rwlock.writeLock();

        try {
            ++counter;
        } finally {
            rwlock.unlockWrite(stamp);
        }
    }
}
