package example.concurrency.producerconsumer.condition;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Consumer {

    private BlockingQueue<String> queue;
    private Lock lock;
    private String name;

    private Condition notEmpty;
    private Condition notFull;

    Consumer(BlockingQueue<String> queue, Lock lock, Condition notEmpty, Condition notFull, String name) {
        this.queue = queue;
        this.lock = lock;
        this.name = name;
        this.notEmpty = notEmpty;
        this.notFull = notFull;
    }

    public String fetch() throws InterruptedException {
        try {
            lock.lock();
            while(queue.size() == 0) {
                notEmpty.await();
            }
            return queue.remove();
        } finally {
            notFull.signalAll();
            lock.unlock();
        }
    }

    public String fetch2() throws InterruptedException {
        try {
            lock.lock();
            while(queue.size() == 0) {
                notEmpty.await();
            }
            String x = queue.remove();
            notFull.signalAll();
            return x;
        } finally {
            lock.unlock();
        }
    }
}
