package example.concurrency.bbuffer.advanced;

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

        // exit when getting interrupted
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (queue) {
                try {
                    System.out.println("<= pop  <= " + name + ": " + queue.take());
                } catch (InterruptedException e) {
                    // restore interruption status
                    Thread.currentThread().interrupt();
                    System.out.println("Being interrupted, give up now: " + name);
                }
            }
        }
    }
}
