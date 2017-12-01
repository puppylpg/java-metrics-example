package example.metrics.old;

import java.util.concurrent.TimeUnit;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Histogram;
import com.yammer.metrics.reporting.ConsoleReporter;

public class TestHistograms {
    private static Histogram histo = Metrics.newHistogram(TestHistograms.class,"histo-sizes");

    public static void main(String[] args) throws InterruptedException {

        ConsoleReporter.enable(1 * 3,TimeUnit.SECONDS);
        int i=0;
        while(true){
//            histo.update(i++);
            final int t = (int)(Math.random() * 100);
            System.out.println("===> t: " + t);
            histo.update(t);
            Thread.sleep(1000 * 3);
        }
    }

}