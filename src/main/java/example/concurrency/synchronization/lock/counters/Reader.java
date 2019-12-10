package example.concurrency.synchronization.lock.counters;

import example.concurrency.synchronization.lock.counters.counter.ReadWriteLockOptimisticStamped;

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

            if (count >= CounterDemo.TARGET_NUMBER || runTimes >= CounterDemo.MAX_READ_TIMES) {
                String failStr = "";
//                if (counter instanceof ReadWriteLockOptimisticStamped) {
//                    long failTimes = ((ReadWriteLockOptimisticStamped) counter).getFailTimes();
//                    failStr = String.format("Fail times: %d. Fail percentage: %f", failTimes, failTimes * 1.0 / runTimes);
//                }
                System.out.println("Reader: read " + runTimes + " times." + failStr);
                CounterDemo.statistic(System.currentTimeMillis(), true, runTimes);
                break;
            }


        }
    }
}
