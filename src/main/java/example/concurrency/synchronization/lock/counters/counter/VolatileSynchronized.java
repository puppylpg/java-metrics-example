package example.concurrency.synchronization.lock.counters.counter;

import example.concurrency.synchronization.lock.counters.Counter;

/**
 * 使用volatile + synchronized，可以做到读不加锁，写加锁！
 * volatile能够保证：
 * 1. happens-before：所以读写有序 & 内存可见；
 * 2. long是64-bit值，加volatile之后，保证本身对long值的读写是原子性的（但是不能保证逻辑上的read-write等好几步操作是原子性的。所以说是：volatile能保证一定的原子性）；
 */
public class VolatileSynchronized implements Counter {
    private volatile long counter;

    public long getCount() {
        return counter;
    }

    public synchronized void increment() {
        // 流弊，如果不给一个写volatile的变量加锁，idea竟然会提醒：non-atomic operation on volatile value
        ++counter;
    }
}