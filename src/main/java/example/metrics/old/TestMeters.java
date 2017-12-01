package example.metrics.old;

import java.util.concurrent.TimeUnit;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.reporting.ConsoleReporter;
import org.apache.commons.lang3.RandomUtils;

public class TestMeters {
    private static Meter meter = Metrics.newMeter(TestMeters.class, "requests","requests", TimeUnit.SECONDS);

    public static void main(String[] args) throws InterruptedException{
        ConsoleReporter.enable(1,TimeUnit.SECONDS);
        /*
         One possible output(the rate will be 8 if time is long enough):

         17-12-1 14:24:59 ===============================================================
         example.metrics.old.TestMeters:
         requests:
         count = 29292
         mean rate = 8.00 requests/s
         1-minute rate = 8.18 requests/s
         5-minute rate = 8.09 requests/s
         15-minute rate = 8.03 requests/s
         */

        while(true){
            // [7, 10)
            meter.mark(RandomUtils.nextInt(7, 10));
            Thread.sleep(1000);
        }
    }
}