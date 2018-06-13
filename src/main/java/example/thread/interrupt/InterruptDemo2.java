package example.thread.interrupt;

/**
 * 当主线程发出interrupt信号的时候，子线程的sleep()被中断，抛出InterruptedException。
 *
 * 在处理该异常的时候，重新设置interrupt flag为true，则在子线程中检测中断flag的时候，成功退出线程。
 *
 * @author liuhaibo on 2018/06/13
 */
public class InterruptDemo2 extends Thread {

    @Override
    public void run() {
        doAPseudoHeavyWeightJob();
    }

    private void doAPseudoHeavyWeightJob() {

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

    private void sleepBabySleep() {
        try {
            Thread.sleep(1000);
            System.out.println("Slept for a while!");
        } catch (InterruptedException e) {
            System.out.println("Interruption happens...");
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        InterruptDemo2 thread = new InterruptDemo2();
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
    Interruption happens...
    5
    IN MAIN:false
    Thread interrupted. Exiting...
    */
}
