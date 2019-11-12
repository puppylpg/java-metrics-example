package example.concurrency.producerconsumer.condition;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer {

    private Queue<String> queue;
    private ReentrantLock lock;
    private String name;

    private Condition notEmpty;
    private Condition notFull;

    Consumer(Queue<String> queue, ReentrantLock lock, String name) {
        this.queue = queue;
        this.lock = lock;
        this.name = name;
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
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
