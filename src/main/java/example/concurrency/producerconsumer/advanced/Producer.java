package example.concurrency.producerconsumer.advanced;

import java.util.concurrent.BlockingQueue;

/**
 * @author liuhaibo on 2018/06/07
 */
public class Producer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private String name;

    private final int NUM = 20;

    Producer(BlockingQueue<Integer> queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name + " start at " + System.currentTimeMillis());
        int i = 0;
        while (i++ < NUM) {
                System.out.println("=> push => " + name + ": " + i);
            try {
                queue.put(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("+++++ EXIT!(" + name + ") +++++");
    }
}
