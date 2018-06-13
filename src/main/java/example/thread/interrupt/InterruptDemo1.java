package example.thread.interrupt;

/**
 * 当主线程发出interrupt信号的时候，子线程的sleep()被中断，抛出InterruptedException。
 *
 * 不处理该异常，直接交到上级caller。上级caller也一直不处理，最后直接整个线程挂了。也相当于成功退出了线程。
 *
 * @author liuhaibo on 2018/06/14
 */
public class InterruptDemo1 extends Thread {

    @Override
    public void run() {
        try {
            doAPseudoHeavyWeightJob();
        } catch (InterruptedException e) {
            System.out.println("=.= I(a thread) am dying...");
            e.printStackTrace();
        }
    }

    private void doAPseudoHeavyWeightJob() throws InterruptedException {

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            System.out.println(i);
            if (i > 10) {
                Thread.currentThread().interrupt();
            }
            // quit if another thread let me interrupt
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Thread interrupted. Exiting...");
                break;
            } else {
                sleepBabySleep();
            }
        }
    }

    private void sleepBabySleep() throws InterruptedException {
            Thread.sleep(1000);
            System.out.println("Slept for a while!");
    }

    public static void main(String[] args) throws InterruptedException {
        InterruptDemo1 thread = new InterruptDemo1();
        thread.start();
        Thread.sleep(5000);
        // let me interrupt
        thread.interrupt();
        System.out.println("IN MAIN:" + thread.isInterrupted());
    }

    /* Output:
    0
    Slept for a while!
    1
    Slept for a while!
    2
    Slept for a while!
    3
    Slept for a while!
    4
    =.= I(a thread) am dying...
    java.lang.InterruptedException: sleep interrupted
    IN MAIN:false
        at java.lang.Thread.sleep(Native Method)
        at example.thread.interrupt.InterruptDemo1.sleepBabySleep(InterruptDemo1.java:36)
        at example.thread.interrupt.InterruptDemo1.doAPseudoHeavyWeightJob(InterruptDemo1.java:30)
        at example.thread.interrupt.InterruptDemo1.run(InterruptDemo1.java:11)
    */
}
