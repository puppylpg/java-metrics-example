package example.metrics.old;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import com.yammer.metrics.reporting.ConsoleReporter;

public class TestTimers {
    private static Timer timer = Metrics.newTimer(TestTimers.class, "responses", TimeUnit.MILLISECONDS,TimeUnit.SECONDS);
    private static Timer errorTimer = Metrics.newTimer(TestTimers.class, "errorTimer", TimeUnit.MILLISECONDS,TimeUnit.SECONDS);

    public static void main(String[] args) throws InterruptedException {
        ConsoleReporter.enable(1,TimeUnit.SECONDS);
        Random rn = new Random();

        int countTmp = 0;
        while(true){
            TimerContext context = timer.time();
            TimerContext errorContext = errorTimer.time();

            int tmp = rn.nextInt(1000);
            countTmp++;
            System.out.println("===> tmp: " + tmp);
            System.out.println("=======>>> countTmp: " + countTmp);
            if(tmp > 700) {
                errorContext.stop();
            }

            Thread.sleep(tmp);
            context.stop();
        }

/*        while (true) {
            TimerContext context = timer.time();
            Thread.sleep(1000);
            context.stop();
        }*/
    }

}