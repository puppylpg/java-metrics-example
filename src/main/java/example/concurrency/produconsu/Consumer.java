package example.concurrency.produconsu;

import java.util.Queue;

/**
 * @author liuhaibo on 2018/4/10
 */
public class Consumer extends Thread {

    private Queue<Integer> queue;
    private String name;

    Consumer(Queue<Integer> queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        System.out.println(name + " start at " + start);

        // 最终，生产者退出后，消费者消费完了最后的数据，wait()，却没人再唤醒他们
        while (true) {
            synchronized (queue) {
                System.out.println("----- I get the lock~(" + name + ") -----");
                while (queue.isEmpty()) {
                    try {
                        System.out.println("----- No elements to consume, release lock, waiting(" + name + ") -----");
                        queue.wait();
                        // 被唤醒之后，如果能拿到锁，是从这里接着继续执行的
                        System.out.println("----- I am awake and get the lock(" + name + ") -----");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("<= pop  <= " + name + ": " + queue.remove());
                System.out.println("----- Hey, get up!(" + name + ") -----");
                queue.notifyAll();
                System.out.println("----- I am gonna release the lock~(" + name + ") -----");
            }
        }
    }
}
