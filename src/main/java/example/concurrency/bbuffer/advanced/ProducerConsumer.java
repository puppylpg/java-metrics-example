package example.concurrency.bbuffer.advanced;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author liuhaibo on 2018/06/07
 */
public class ProducerConsumer {

    public static void main(String [] args) throws InterruptedException {
        int maxSize = 5;
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(maxSize);
        Thread p1 = new Thread(new Producer(queue, "p1"));
        Thread c1 = new Thread(new Consumer(queue, "c1"));
        Thread c2 = new Thread(new Consumer(queue, "c2"));
        p1.start();
        c1.start();
        c2.start();
        Thread.sleep(3 * 1000);

        // inform consumers to stop, but need consumers to cooperate
        c1.interrupt();
        c2.interrupt();
    }

    /*
        p1 start at 1539667954363
        c1 start at 1539667954363
        => push => p1: 1
        c2 start at 1539667954364
        => push => p1: 2
        => push => p1: 3
        <= pop  <= c1: 1
        => push => p1: 4
        <= pop  <= c1: 2
        => push => p1: 5
        <= pop  <= c1: 3
        => push => p1: 6
        <= pop  <= c1: 4
        => push => p1: 7
        => push => p1: 8
        <= pop  <= c1: 5
        => push => p1: 9
        <= pop  <= c1: 6
        => push => p1: 10
        <= pop  <= c1: 7
        => push => p1: 11
        <= pop  <= c1: 8
        => push => p1: 12
        <= pop  <= c1: 9
        => push => p1: 13
        <= pop  <= c1: 10
        => push => p1: 14
        <= pop  <= c1: 11
        => push => p1: 15
        <= pop  <= c1: 12
        => push => p1: 16
        <= pop  <= c1: 13
        => push => p1: 17
        <= pop  <= c1: 14
        => push => p1: 18
        <= pop  <= c1: 15
        => push => p1: 19
        <= pop  <= c1: 16
        => push => p1: 20
        <= pop  <= c1: 17
        +++++ EXIT!(p1) +++++
        <= pop  <= c1: 18
        <= pop  <= c1: 19
        <= pop  <= c1: 20
        Being interrupted, give up now: c1
        Being interrupted, give up now: c2
     */
}
