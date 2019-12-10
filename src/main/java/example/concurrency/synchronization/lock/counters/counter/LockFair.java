package example.concurrency.synchronization.lock.counters.counter;


import example.concurrency.synchronization.lock.counters.Counter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockFair implements Counter {
    private Lock lock = new ReentrantLock(true);


    private long counter;

    public long getCount() {
        lock.lock();
        try {
            return counter;
        } finally {
            lock.unlock();
        }
    }

    public void increment() {
        lock.lock();
        try {
            ++counter;
        } finally {
            lock.unlock();
        }
    }
}
