package example.concurrency.forkjoin;

/**
 * @author liuhaibo on 2019/11/25
 */
public class BaseCalculator {

    /**
     * 叠加到100*1亿：
     * sum=-5340232226128654848, time used=4574 ms.
     * 叠加到1000*1亿：
     * sum=932355974711512064, time used=45324 ms.
     * 叠加到10000*1亿：
     * sum=1001881602603448320, time used=440411 ms.
     */
    public static void main(String... args) {
        long size = 100000000 * 10000L;

        long sum = 0;
        long start = System.currentTimeMillis();
        for (long i = 0; i < size; i++) {
            sum += i;
        }
        long end = System.currentTimeMillis();
        System.out.printf("sum=%d, time uased=%d ms.", sum, end - start);
    }
}
