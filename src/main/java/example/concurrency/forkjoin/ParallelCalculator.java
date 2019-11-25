package example.concurrency.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author liuhaibo on 2019/11/25
 */
public class ParallelCalculator {

    /**
     * 叠加到100*1亿：
     * sum=-5340232226128654848, time used=6291 ms.
     * 叠加到1000*1亿：
     * sum=932355974711512064, time used=44028 ms.
     * 叠加到10000*1亿：
     * sum=1001881602603448320, time used=611418 ms.
     *
     * （以100为threshold）都不比直接单线程叠加快，估计是fork太多了。
     *
     * 同样叠加到10000*1亿，threshold = 1亿：
     * sum=1001881602603448320, time used=152406 ms.
     *
     * 比threshold=100少用了80%的时间，只占用直接单线程叠加时间的35%不到
     */
    public static void main(String... args) throws ExecutionException, InterruptedException {
        long size = 100000000 * 10000L;

        long start = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();
        Future<Long> result = pool.submit(new Calculator(0, size));

        long sum = result.get();
        long end = System.currentTimeMillis();
        System.out.printf("sum=%d, time used=%d ms.", sum, end - start);
    }

    private static class Calculator extends RecursiveTask<Long> {

        private static final long THRESHOLD = 100000000 * 100L;
        private long start;
        private long end;

        Calculator(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            long sum = 0;
            if ((end - start) < THRESHOLD) {
                for (long i = start; i < end; i++) {
                    sum += i;
                }
            } else {
                long middle = (start + end) / 2;
                Calculator left = new Calculator(start, middle);
                Calculator right = new Calculator(middle, end);
                left.fork();
                right.fork();

                sum = left.join() + right.join();
            }
            return sum;
        }
    }
}
