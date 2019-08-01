package example.concurrency.bbuffer.manually;

import java.util.Queue;

/**
 * 与自旋等待或休眠等待的实现相比，条件队列没有改变原来的语义，且在多个方面进行了优化：
 * cpu效率、上下文切换开销、响应性等。
 *
 * 如果某个功能通过自旋等待或者轮询+休眠无法实现，那么可粗略认为条件队列也无法实现。
 *
 * @see example.concurrency.bbuffer.immature.SpinningBoundedBuffer
 * @see example.concurrency.bbuffer.immature.SleepyBoundedBuffer
 *
 * @author liuhaibo on 2018/4/10
 */
public class Producer extends Thread {

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
        super.setName(name);
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
                // 只要不释放锁，被唤醒的线程就不会执行。不用担心notify到释放锁的时间太长，其他线程得不到锁又wait了。。。
                // 这不是“醒来”，“醒来”更适合表述sleep
            }
        }
        System.out.println("+++++ EXIT!(" + name + ") +++++");
    }
}
