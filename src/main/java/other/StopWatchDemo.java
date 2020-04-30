package other;

import org.apache.commons.lang3.time.StopWatch;

/**
 * @author liuhaibo on 2020/04/22
 */
public class StopWatchDemo {

    public static void main(String... args) throws InterruptedException {
        StopWatch stopWatch = StopWatch.createStarted();

        Thread.sleep(1);
        System.out.println(gapMilliSeconds(stopWatch));
        Thread.sleep(2);
        System.out.println(gapMilliSeconds(stopWatch));
        Thread.sleep(5);
        System.out.println(gapMilliSeconds(stopWatch));
    }

    private static long gapMilliSeconds(StopWatch stopWatch) {
        try {
            stopWatch.stop();
            return stopWatch.getTime();
        } finally {
            stopWatch.reset();
            stopWatch.start();
        }
    }
}
