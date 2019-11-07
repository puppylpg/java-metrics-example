package example.concurrency.synchronization.lock.counters;

public class Reader implements Runnable {
    private final Counter counter;

    public Reader(Counter counter) {
        this.counter = counter;
    }

    public void run() {
        while (true) {
            if (Thread.interrupted()) {
                break;
            }

            long count = counter.getCount();

            if (count > MultiImplementationCounterDemo.TARGET_NUMBER) {
                MultiImplementationCounterDemo.publish(System.currentTimeMillis());
                break;
            }
        }
    }
}
