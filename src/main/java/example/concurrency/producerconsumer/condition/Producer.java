package example.concurrency.producerconsumer.condition;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Producer {

    private Queue<String> queue;
    private int queueSize;
    private Lock lock;
    private String name;

    private Condition stackNotEmptyCondition;
    private Condition stackNotFullCondition;

    Producer(BlockingQueue<String> queue, int queueSize, Lock lock, Condition notEmpty, Condition notFull, String name) {
        this.queue = queue;
        this.queueSize = queueSize;
        this.lock = lock;
        this.name = name;
        this.stackNotEmptyCondition = notEmpty;
        this.stackNotFullCondition = notFull;
    }

    /**
     * 和wait/notifyAll一样的步骤：
     * 1. 加锁。这次用的是显示锁，而不是把临界区资源当成锁（同时）；
     * 2. 判断是否满了；
     * 3. 没满就放，满了就await；
     * @param item
     * @throws InterruptedException
     */
    public void add(String item) throws InterruptedException {
        try {
            lock.lock();
            while(queue.size() == queueSize) {
                stackNotFullCondition.await();
            }
            queue.add(item);
            stackNotEmptyCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
