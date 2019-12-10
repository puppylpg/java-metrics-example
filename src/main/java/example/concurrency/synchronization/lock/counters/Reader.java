package example.concurrency.synchronization.lock.counters;

public class Reader implements Runnable {
    private final Counter counter;

    private long runTimes = 0;

    public Reader(Counter counter) {
        this.counter = counter;
    }

    public void run() {
        while (true) {
            if (Thread.interrupted()) {
                break;
            }

            long count = counter.getCount();
            runTimes++;

            if (count > CounterDemo.TARGET_NUMBER) {
                System.out.println("Reader: read " + runTimes + " times.");
                CounterDemo.statistic(System.currentTimeMillis(), true, runTimes);
                break;
            }


        }
    }
}
