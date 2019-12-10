package example.concurrency.synchronization.lock.counters.counter;


import example.concurrency.synchronization.lock.counters.Counter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDefault implements Counter {
    private java.util.concurrent.locks.ReadWriteLock rwlock = new ReentrantReadWriteLock();

    private Lock rlock = rwlock.readLock();
    private Lock wlock = rwlock.writeLock();

    private long counter;

    public long getCount() {
        rlock.lock();
        try {
            return counter;
        } finally {
            rlock.unlock();
        }
    }

    public void increment() {
        wlock.lock();
        try {
            ++counter;
        } finally {
            wlock.unlock();
        }
    }
}
