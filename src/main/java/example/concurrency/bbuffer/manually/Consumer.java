package example.concurrency.bbuffer.manually;

import java.util.Queue;

/**
 * @author liuhaibo on 2018/4/10
 */
public class Consumer extends Thread {

    // 锁对象最好定义成final，要不然如果一个线程正在调用锁，
    // 另一个通过setQueue把queue给换了，gg，这时候另一个线程
    // 会发现拿到了新的queue的锁，然后两个线程就同时执行本来应该被锁住的代码块了
    private final Queue<Integer> queue;
    private String name;

    Consumer(Queue<Integer> queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        System.out.println(name + " start at " + start);

        // exit when getting interrupted
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (queue) {
                System.out.println("----- I get the lock~(" + name + ") -----");
                while (queue.isEmpty()) {
                    try {
                        System.out.println("----- No elements to consume, release lock, waiting(" + name + ") -----");
                        queue.wait();
                        // 被唤醒之后，如果能拿到锁，是从这里接着继续执行的
                        System.out.println("----- I am awake and get the lock(" + name + ") -----");
                    } catch (InterruptedException e) {
                        // restore interruption status
                        Thread.currentThread().interrupt();
                        System.out.println("Being interrupted, give up now: " + name);
                        break;
                    }
                }
                if (!queue.isEmpty()) {
                    System.out.println("<= pop  <= " + name + ": " + queue.remove());
                    System.out.println("----- Hey, get up!(" + name + ") -----");
                    queue.notifyAll();
                    System.out.println("----- I am gonna release the lock~(" + name + ") -----");
                }
            }
        }
    }
}
