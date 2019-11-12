package example.concurrency.producerconsumer.immature;

/**
 * 重试方式一：忙等待。
 *
 * <p>采用忙等待（自旋等待）方式不断重试。但是如果短期内队列状态没有变化，会消耗大量的cpu时间。
 * 如果选择让出cpu（比如休眠）而不是消耗完整个cpu时间片，整体会更高效。但是如果休眠时间过长，又会降低响应性。
 *
 * @see example.concurrency.producerconsumer.waitnotify.Producer
 *
 * @author puppylpg on 2018/11/29
 */
public class SpinningBoundedBuffer<V> extends BasedBoundedBuffer<V> {

    protected SpinningBoundedBuffer(int capacity) {
        super(capacity);
    }

    /**
     * {@inheritDoc}。
     * <p>如果队列已满，直接重试。
     *
     * @throws InterruptedException 重试时被中断
     */
    public void put(V v) throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (this) {
                if (!isFull()) {
                    doPut(v);
                    return;
                }
            }
        }
        throw new InterruptedException();
    }

    /**
     * {@inheritDoc}。
     * <p>如果队列已满，直接重试。
     *
     * @return 队首的值
     * @throws InterruptedException 重试时被中断
     */
    public V take() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (this) {
                if (!isEmpty()) {
                    return doTake();
                }
            }
        }
        throw new InterruptedException();
    }
}
