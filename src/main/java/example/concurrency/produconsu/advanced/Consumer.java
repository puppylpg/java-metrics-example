package example.concurrency.produconsu.advanced;

import java.util.concurrent.BlockingQueue;

/**
 * @author liuhaibo on 2018/06/07
 */
public class Consumer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private String name;

    Consumer(BlockingQueue<Integer> queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        System.out.println(name + " start at " + start);

        while (true) {
            synchronized (queue) {
                try {
                    System.out.println("<= pop  <= " + name + ": " + queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
