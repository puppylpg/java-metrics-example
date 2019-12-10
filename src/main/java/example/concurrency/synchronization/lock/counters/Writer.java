package example.concurrency.synchronization.lock.counters;

public class Writer implements Runnable {
    private final Counter counter;
    private long runTimes = 0;

    public Writer(Counter counter) {
        this.counter = counter;
    }

    public void run() {
        while (true) {
            if (Thread.interrupted()) {
                break;
            }

            if (counter.getCount() > CounterDemo.TARGET_NUMBER) {
                System.out.println("Writer: write " + runTimes + " times.");
                CounterDemo.statistic(System.currentTimeMillis(), false, runTimes);
                break;
            }
            counter.increment();
            runTimes++;
        }
    }
}
