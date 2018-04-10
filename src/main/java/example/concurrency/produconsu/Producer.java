package example.concurrency.produconsu;

import java.util.Queue;

/**
 * @author liuhaibo on 2018/4/10
 */
public class Producer extends Thread{

    private Queue<Integer> queue;
    private int maxSize;
    private String name;
    private int totalNum;

    Producer(Queue<Integer> queue, int maxSize, String name, int totalNum) {
        this.queue = queue;
        this.maxSize = maxSize;
        this.name = name;
        this.totalNum = totalNum;
    }

    @Override
    public void run() {
        System.out.println(name + " start at " + System.currentTimeMillis());
        int i = 0;
        while (i++ < totalNum) {
            synchronized (queue) {
                System.out.println("+++++ I get the lock~(" + name + ") +++++");
                while (queue.size() >= maxSize) {
                    try {
                        System.out.println("+++++ No space to produce, release lock, waiting(" + name + ") +++++");
                        queue.wait();
                        System.out.println("+++++ I am awake and get the lock(" + name + ") +++++");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("=> push => " + name + ": " + i);
                queue.add(i);
                System.out.println("+++++ Hey, get up!(" + name + ") +++++");
                queue.notifyAll();
                System.out.println("+++++ I am gonna release the lock~(" + name + ") +++++");
            }
        }
        System.out.println("+++++ EXIT!(" + name + ") +++++");
    }
}
