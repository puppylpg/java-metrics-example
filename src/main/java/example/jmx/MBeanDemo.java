package example.jmx;

import lombok.AllArgsConstructor;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * JMX(Java Management Extensions) provides an architecture to manage resources dynamically at runtime.
 *
 * It is used mostly in enterprise applications to
 * 1. get the state of application at any point of time;
 * 2. make the system configurable.
 *
 * 运行时配置：用JMX把需要配置的属性集中在一个类中，然后写一个MBean，再进行相关配置。另外JMX还提供了一个工具页，以方便我们对参数值进行修改。
 *
 * 注意事项：
 * 1. MBean的命名，见注释；
 * 2. 接口中，所有get/set开头的方法被认为是操作属性的getter/setter，可在Jconsole里直接操作属性；
 * 3. 接口中，非get/set开头的public方法可在Jconsole里直接调用，直接在程序运行时操作程序！
 *
 * @author liuhaibo on 2018/01/04
 */
public class MBeanDemo {

    public interface CountDownMBean {
        public String getName();

        public int getCounter();

        /**
         * counter +1
         */
        public void increase();

        /**
         * change the value of counter
         *
         * @param counter counter
         */
        public void changeCounter(int counter);

        /**
         * this is 'Getter', and can be applied on counter directly
         *
         * @param counter
         */
        public void setCounter(int counter);
    }

    /**
     * The JMX Naming convention is to keep the implementation class name as
     * "interface name - 'MBean'", so it must be 'CountDown'.
     */
    @AllArgsConstructor
    public static class CountDown implements CountDownMBean {

        String name;

        int counter;

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getCounter() {
            return this.counter;
        }

        @Override
        public void increase() {
            System.out.println("counter +1");
            counter++;
        }

        @Override
        public void changeCounter(int counter) {
            System.out.println("counter is changed into: " + counter);
            this.counter = counter;
        }

        @Override
        public void setCounter(int counter) {
            System.out.println("counter is set to: " + counter);
            this.counter = counter;
        }

        /**
         * not in interface, so not display in Jconsole
         */
        public void decrease() {
            counter--;
        }
    }

    private static final String NAME = "puppylpg";
    private static final int COUNTER = 100;

    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, InterruptedException {

        // get MBean server
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        // new my MBean
        CountDown countDown = new CountDown(NAME, COUNTER);
        // name for my MBean
        ObjectName mbeanName = new ObjectName("puppylpg.com:type=CountDown");
        // register my MBean
        mbs.registerMBean(countDown, mbeanName);

        while (countDown.getCounter() != 0) {
            countDown.decrease();
            Thread.sleep(3 * 1000);
            System.out.println("name: " + countDown.getName() + "; count down: " + countDown.getCounter());
        }
    }
}