package example.singleton;

import example.singleton.instances.DoubleCheck;
import example.singleton.instances.Perfect;
import example.singleton.instances.Simplest;
import example.singleton.instances.StaticInnerClass;

/**
 * @author liuhaibo on 2017/12/26
 */
public class SingletonDemo {

    private static Simplest simplest = Simplest.getInstance();

    private static DoubleCheck doubleCheck = DoubleCheck.getInstance();

    private static StaticInnerClass staticInnerClass = StaticInnerClass.getInstance();

    private static Perfect perfect = Perfect.INSTANCE;

    public static void main(String [] avgs) {
        System.out.println(simplest.doSomething());
        System.out.println(doubleCheck.doSomething());
        System.out.println(staticInnerClass.doSomething());
        System.out.println(perfect.doSomething());
    }
}