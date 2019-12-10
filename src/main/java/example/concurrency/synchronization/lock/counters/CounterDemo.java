package example.concurrency.synchronization.lock.counters;

import example.concurrency.synchronization.lock.counters.counter.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * reader {@link Counter#getCount()}，writer {@link Counter#increment()}，
 * 加到{@link CounterDemo#TARGET_NUMBER}就结束。
 */
public class CounterDemo {
    static long TARGET_NUMBER = 1000000L;
    static long MAX_READ_TIMES = TARGET_NUMBER * 10;
    private static int READ_THREADS = 5;
    private static int WRITE_THREADS = 1;

    private static ExecutorService es;

    private static long start;

    /**
     * 使用array保存一次结果的状态。
     * 包括退出条件，总读取次数，总写入次数
     */
    private static AtomicLongArray syncInfo;
    private static int doneThreadNums = 0;
    private static int totalReadTimes = 1;
    private static int totalWriteTimes = 2;

    private enum CounterType {
        DIRTY,
        ATOMIC,
        ADDER,
        SYNCHRONIZED,
        VOLATILE_SYNCHRONIZED,
        LOCK,
//        LOCK_FAIR,
        READ_WRITE_LOCK,
        READ_WRITE_LOCK_STAMPED,
        READ_WRITE_LOCK_OPTIMISTIC
    }

    private static void reset() {
        syncInfo.set(doneThreadNums, 0L);
        syncInfo.set(totalReadTimes, 0L);
        syncInfo.set(totalWriteTimes, 0L);
    }

    public static void main(String[] args) {
        syncInfo = new AtomicLongArray(3);

        System.out.printf("Using read threads: %d, write threads: %d. Target number: %d.\n\n", READ_THREADS, WRITE_THREADS, TARGET_NUMBER);

        for (Counter counter : getCounters()) {
            reset();

            System.out.println("Counter: " + counter);

            es = Executors.newFixedThreadPool(READ_THREADS + WRITE_THREADS);
            System.out.println("Init ExecutorService successfully.");

            start = System.currentTimeMillis();

            for (int j = 0; j < READ_THREADS; j++) {
                es.execute(new Reader(counter));
            }

            for (int i = 0; i < WRITE_THREADS; i++) {
                es.execute(new Writer(counter));
            }

            try {
                es.awaitTermination(10, TimeUnit.MINUTES);
                System.out.println("ExecutorService shutdown successfully.");
                System.out.println();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Counter[] getCounters() {
        Counter[] counters = new Counter[CounterType.values().length];
        int i = 0;
        for (CounterType counterType : CounterType.values()) {
            counters[i] = getCounter(counterType);
            i++;
        }
        return counters;
    }

    private static Counter getCounter(CounterType counterType) {

        switch (counterType) {
            case ADDER:
                return new Adder();
            case ATOMIC:
                return new Atomic();
            case DIRTY:
                return new Dirty();
            case SYNCHRONIZED:
                return new Synchronized();
            case VOLATILE_SYNCHRONIZED:
                return new VolatileSynchronized();
            case LOCK:
                return new LockDefault();
//            case LOCK_FAIR:
//                return new LockFair();
            case READ_WRITE_LOCK:
                return new ReadWriteLockDefault();
            case READ_WRITE_LOCK_STAMPED:
                return new ReadWriteLockStamped();
            case READ_WRITE_LOCK_OPTIMISTIC:
                return new ReadWriteLockOptimisticStamped();
            default:
                return null;
        }
    }

    static void statistic(long end, boolean reader, long runTimes) {
        if (reader) {
            syncInfo.addAndGet(totalReadTimes, runTimes);
        } else {
            syncInfo.addAndGet(totalWriteTimes, runTimes);
        }
        // 所有线程均结束的话，再关闭ExecutorService
        if (syncInfo.incrementAndGet(doneThreadNums) == READ_THREADS + WRITE_THREADS) {
            System.out.println("Time used: " + (end - start) + "ms");
            System.out.println("Total read times: " + syncInfo.get(totalReadTimes));
            System.out.println("Total write times: " + syncInfo.get(totalWriteTimes));
            es.shutdownNow();
        }
    }
}
