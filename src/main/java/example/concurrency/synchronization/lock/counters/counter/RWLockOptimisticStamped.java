package example.concurrency.synchronization.lock.counters.counter;

import example.concurrency.synchronization.lock.counters.Counter;

import java.util.concurrent.locks.StampedLock;

public class RWLockOptimisticStamped implements Counter {

    private StampedLock rwlock = new StampedLock();

    private long counter;

    private long readTimes;
    private long successTimes;

    /**
     * 乐观锁用法：先去读，读完看看改了没（乐观：赌一把，我猜大概率没改），
     * 没改就幸灾乐祸。改了就重试。或者说，用read lock。
     */
    public long getCount() {
        long stamp = rwlock.tryOptimisticRead();

        readTimes++;

        if (rwlock.validate(stamp)) {
            successTimes++;
            return counter;
        } else {
            long readStamp = rwlock.readLock();
            try {
                return counter;
            } finally {
                rwlock.unlock(readStamp);
            }
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

    public long getSuccessTimes() {
        return successTimes;
    }


    public long getReadTimes() {
        return readTimes;
    }
}
