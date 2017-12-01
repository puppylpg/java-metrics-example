package example.metrics.old;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.reporting.ConsoleReporter;

public class TestGauges {
    public static Queue<String> queue = new LinkedList<String>();

    public static void main(String[] args) throws InterruptedException{
        ConsoleReporter.enable(5,TimeUnit.SECONDS);

        Gauge<Integer> g = Metrics.newGauge(TestGauges.class, "pending-jobs", new Gauge<Integer>() {
            @Override
            public Integer value() {
                return queue.size();
            }
        });
        queue.add("ssss");
        System.out.println(g.value() + "Liu Haibo");
        while(true){
            queue.add("a");
            System.out.println(g.value() + "Liu Haibo");
            Thread.sleep(1000);
        }
    }
}