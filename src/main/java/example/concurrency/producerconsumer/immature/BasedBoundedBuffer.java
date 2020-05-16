package example.concurrency.producerconsumer.immature;

/**
 * @author puppylpg on 2018/11/29
 */
abstract class BasedBoundedBuffer<V> implements BoundedBuffer<V> {

    private final V[] buffer;
    private int tail, head, count;

    @SuppressWarnings("unchecked")
    protected BasedBoundedBuffer(int capacity) {
        buffer = (V[]) new Object[capacity];
    }

    protected final void doPut(V v) {
        buffer[tail] = v;
        if (++tail == buffer.length) {
            tail = 0;
        }
        ++count;
        System.out.println("put: " + v);
    }

    protected final V doTake() {
        V v = buffer[head];
        buffer[head] = null;
        if (++head == buffer.length) {
            head = 0;
        }
        --count;
        System.out.println("get: " + v);
        return v;
    }

    public synchronized final boolean isFull() {
        return count == buffer.length;
    }

    public synchronized final boolean isEmpty() {
        return count == 0;
    }
}
