package example.concurrency.producerconsumer.immature;

/**
 * 重试方式二：轮询+休眠
 *
 * <p>存在和{@link SpinningBoundedBuffer}一样的权衡问题。
 *
 * <p>注意：一定不要带着锁去sleep！
 *
 * <p>sleep是属于Thread的方法，跟锁无关。wait才是跟锁相关的方法，会释放锁。
 * 这一点只要清楚sleep和wait的区别，清楚Object的这些方法（操作某个object的条件队列）
 * 和Thread的方法（操作某个thread）的区别，就不会搞错。
 *
 * @see example.concurrency.producerconsumer.waitnotify.Producer
 *
 * @author puppylpg on 2018/11/29
 */
public class SleepyBoundedBuffer<V> extends BasedBoundedBuffer<V> {

    private int interval = 100;

    protected SleepyBoundedBuffer(int capacity) {
        super(capacity);
    }

    protected SleepyBoundedBuffer(int capacity, int interval) {
        super(capacity);
        this.interval = interval;
    }

    /**
     * {@inheritDoc}
     * <p>如果队列已满，线程休眠一段时间，并重试。
     *
     * @throws InterruptedException 休眠中断
     */
    public void put(V v) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isFull()) {
                    doPut(v);
                    return;
                }
            }
            Thread.sleep(interval);
        }
    }

    /**
     * {@inheritDoc}。
     * <p>如果队列已满，线程休眠一段时间，并重试。
     *
     * @throws InterruptedException 休眠中断
     */
    public V take() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isEmpty()) {
                    return doTake();
                }
            }
            Thread.sleep(interval);
        }
    }
}
