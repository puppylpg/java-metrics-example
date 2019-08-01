package example.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * {@link ThreadFactory} can decouple creation of thread and using of thread,
 * so that one can customize his own threads.
 *
 * 线程的创建，尽量使用{@link ThreadFactory}
 *
 * @author liuhaibo on 2019/07/31
 */
public class ThreadFactoryDemo {

    private static Runnable nothing = () -> {};

    public static void main(String... args) {
        defaultThreadFactoryName();
        simplestThreadFactoryName();
    }

    private static void defaultThreadFactoryName() {
        // first thread factory: pool-1
        ThreadFactory threadFactory1 = Executors.defaultThreadFactory();
        // pool-1-thread-1
        Thread thread11 = threadFactory1.newThread(nothing);
        System.out.println(thread11.getName());
        // pool-1-thread-2
        Thread thread12 = threadFactory1.newThread(nothing);
        System.out.println(thread12.getName());

        // second thread factory: pool-2
        ThreadFactory threadFactory2 = Executors.defaultThreadFactory();
        // pool-2-thread-1
        Thread thread21 = threadFactory2.newThread(nothing);
        System.out.println(thread21.getName());
        // pool-2-thread-2
        Thread thread22 = threadFactory2.newThread(nothing);
        System.out.println(thread22.getName());

        // TODO: ThreadGroup
        System.out.println(thread22.getThreadGroup());
    }

    private static void simplestThreadFactoryName() {
        ThreadFactory threadFactory = new SimplestThreadFactory();
        // Thread-n
        System.out.println(threadFactory.newThread(nothing).getName());
        System.out.println(threadFactory.newThread(nothing).getName());
    }

    /**
     * Simplest.
     */
    static class SimplestThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    }
}
