package example.metrics;

import com.codahale.metrics.*;

import java.util.concurrent.TimeUnit;

/**
 * @author liuhaibo on 2017/12/01
 */
public class MetricsDemo {

    public static void main(String[] args) {

        //////////////////////////////////// START GET REGISTRY //////////////////////////////////////////////

        final String GUI_LIN = "liuguilin";

        MetricRegistry registry1 = new MetricRegistry();
        // get Metrics in a better way
        MetricRegistry registry2 = SharedMetricRegistries.getOrCreate(GUI_LIN);

        //////////////////////////////////// END GET REGISTRY //////////////////////////////////////////////


        //////////////////////////////////// START GET METRICS //////////////////////////////////////////////

        // first way
        Meter aMeter1 = new Meter();
        registry1.register("FirstMeter", aMeter1);
        // second way
        Meter aMeter2 = registry1.meter("SecondMeter");

        String meterName = MetricRegistry.name(Thread.currentThread().getClass(), "request", "ThirdMeter");
        Meter aMeter3 = registry1.meter(meterName);

        Meter bMeter = registry2.meter("FirstMeter");

        //////////////////////////////////// END GET METRICS //////////////////////////////////////////////

        //////////////////////////////////// START MARK METRICS //////////////////////////////////////////////

        aMeter1.mark(100);
        aMeter2.mark(200);
        aMeter3.mark(300);

        bMeter.mark(1000);

        //////////////////////////////////// END MARK METRICS //////////////////////////////////////////////

        // registers : singleton & thread-safe
        SharedMetricRegistries.add("liuhaibo", registry1);
        MetricRegistry otherRegister = SharedMetricRegistries.getOrCreate("liuhaibo");

        //////////////////////////////////// START GET REPORTER AND REGISTER A REGISTRY //////////////////////

        // conReporter: all the metrics in the same registry1 have the same rules
        ConsoleReporter conReporter = ConsoleReporter.forRegistry(registry1).build();
        conReporter.start(1 * 1000, TimeUnit.MILLISECONDS);

        JmxReporter jmxReporter = JmxReporter.forRegistry(registry2).build();
        jmxReporter.start();

        //////////////////////////////////// END GET REPORTER AND REGISTER A REGISTRY //////////////////////

        try {
            Thread.sleep(50 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        conReporter.report();
    }
}