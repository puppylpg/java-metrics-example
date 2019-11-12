package example.concurrency.producerconsumer.condition;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * {@link java.util.concurrent.ArrayBlockingQueue}的阻塞方法put和take方法使用的是
 * {@link java.util.concurrent.locks.Condition}的await和signal，
 * 而非{@link Object}的wait/notify。
 */
public class ConditionProducerConsumer {

    public static void main(String [] args) {
        int maxSize = 5;
        Queue<Integer> queue = new ArrayDeque<>(maxSize);

    }
}
