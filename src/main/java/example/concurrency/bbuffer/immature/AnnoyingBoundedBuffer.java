package example.concurrency.bbuffer.immature;

import example.concurrency.bbuffer.immature.exception.BufferEmptyException;
import example.concurrency.bbuffer.immature.exception.BufferFullException;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * 当队列处于错误状态时，抛出异常（或者返回错误值）给使用带来了复杂性，
 * 每次调用都要捕获异常（或者判断返回值）。
 *
 * <p>一种更合理的实现是尝试进行重试，如{@link SpinningBoundedBuffer}、{@link SleepyBoundedBuffer}。
 *
 * <p>这也是{@link Queue}不适合在生产者-消费者中使用的原因：
 * 它在队列为空时只提供了{@link Queue#poll()} - 返回null，{@link Queue#remove()} - 抛出异常。
 * 而{@link BlockingQueue}则提供了{@link BlockingQueue#take()} - 只有队列处于正确状态时才处理，否则阻塞。
 * 所以{@link BlockingQueue}才是生产者-消费者模式更好的选择。
 *
 * @author puppylpg on 2018/11/29
 */
public class AnnoyingBoundedBuffer<V> extends BasedBoundedBuffer<V> {

    protected AnnoyingBoundedBuffer(int capacity) {
        super(capacity);
    }

    /**
     * {@inheritDoc}
     * <p>队列满时直接抛异常。
     *
     * @throws BufferFullException 队列满异常
     */
    public synchronized void put(V v) throws BufferFullException {
        if (isFull()) {
            throw new BufferFullException();
        }
        doPut(v);
    }

    /**
     * {@inheritDoc}
     * <p>队列空时直接抛异常
     *
     * @throws BufferEmptyException 队列空异常
     */
    public synchronized V take() throws BufferEmptyException {
        if (isEmpty()) {
            throw new BufferEmptyException();
        }
        return doTake();
    }
}
