package example.concurrency.produconsu.manually;

import java.util.Queue;

/**
 * @author liuhaibo on 2018/4/10
 */
public class Producer extends Thread{

    // 锁对象最好定义成final，要不然如果一个线程正在调用锁，
    // 另一个通过setQueue把queue给换了，gg，这时候另一个线程
    // 会发现拿到了新的queue的锁，然后两个线程就同时执行本来锁住的代码块了
    private final Queue<Integer> queue;
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
