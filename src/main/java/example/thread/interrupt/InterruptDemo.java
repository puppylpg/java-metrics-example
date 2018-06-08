package example.thread.interrupt;

/**
 * A thread can't close another thread, but can interrupt it.
 *
 * @author liuhaibo on 2018/06/08
 */
public class InterruptDemo {

    /**
     * Many methods that throw InterruptedException, such as sleep(),
     * are designed to cancel their current operation and return immediately
     * when an interrupt is received.
     *
     * @return a task
     */
    private static Runnable blockedMethodTask() {
        return () -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    System.out.println(i);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("Oops, I'm interrupted when sleeping. Time to stop.");
                    return;
                }
            }
        };
    }

    /**
     * What if a thread goes a long time without invoking a method that throws InterruptedException?
     * Then it must periodically invoke Thread.interrupted() (This is a static method),
     * which returns true if an interrupt has been received.
     *
     * @return a task
     */
    private static Runnable longTimeTask() {
        return () -> {
            while (true) {
                System.out.println("I'm doing a long-time task");
                if (Thread.interrupted()) {
                    System.out.println("Oops, I'm interrupted when calculating. Time to stop.");
                    return;
                }
            }
        };
    }

    public static void main(String... args) throws InterruptedException {
        Thread t1 = new Thread(blockedMethodTask());
        Thread t2 = new Thread(longTimeTask());

        t1.start();
        Thread.sleep(2_000);
        t1.interrupt();
        System.out.println("Haha, t1 is interrupted by me~");

        t2.start();
        t2.interrupt();
        System.out.println("Haha, t2 is interrupted by me~");

        // this is a non-static method, invoked by thread object
        if (t2.isInterrupted()) {
            System.out.println("t2 is at the state of interrupted now.");
        }
    }
}
