package example.metrics.old;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.reporting.ConsoleReporter;

public class TestCounter {

    private final Counter pendingJobs = Metrics.newCounter(TestCounter.class, "pending-jobs");
    private final Queue<String> queue = new LinkedList<String>();

    public void add(String str) {
        pendingJobs.inc();
        queue.offer(str);
    }

    public String take() {
        pendingJobs.dec();
        return queue.poll();
    }

    public static void main(String[]args) throws InterruptedException {
        // TODOAuto-generated method stub
        TestCounter tc = new TestCounter();
        ConsoleReporter.enable(1,TimeUnit.SECONDS);

        boolean flag = true;

        while(true){
            if(flag) {
                tc.add("1");
                tc.add("2");
            } else {
                tc.take();
            }
            flag = !flag;
            Thread.sleep(1000);
        }
    }

}