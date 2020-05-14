package example.concurrency.producerconsumer.condition;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@link java.util.concurrent.ArrayBlockingQueue}的阻塞方法put和take方法使用的是
 * {@link java.util.concurrent.locks.Condition}的await和signal，
 * 而非{@link Object}的wait/notify。
 */
public class ConditionProducerConsumer {

    public static void main(String [] args) {
        int maxSize = 5;
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(maxSize);
        Lock lock = new ReentrantLock(false);
        Condition notEmpty = lock.newCondition();
        Condition notFull = lock.newCondition();
        // TODO: maxsize vs. queue has size
        Producer p1 = new Producer(queue, maxSize, lock, notEmpty, notFull, "p1");
        Consumer c1 = new Consumer(queue, lock, notEmpty, notFull, "c1");
    }
}
