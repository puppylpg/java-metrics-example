package example.concurrency.produconsu.advanced;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author liuhaibo on 2018/06/07
 */
public class ProducerConsumer {

    public static void main(String [] args) {
        int maxSize = 5;
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(maxSize);
        new Thread(new Producer(queue, "p1")).start();
        new Thread(new Consumer(queue, "c1")).start();
        new Thread(new Consumer(queue, "c2")).start();
    }

    /*
    p1 start at 1528348040743
            => push => p1: 1
            => push => p1: 2
            => push => p1: 3
            => push => p1: 4
            => push => p1: 5
            => push => p1: 6
    c1 start at 1528348040744
    c2 start at 1528348040744
            <= pop  <= c1: 1
            => push => p1: 7
            <= pop  <= c1: 2
            => push => p1: 8
            <= pop  <= c1: 3
            => push => p1: 9
            <= pop  <= c1: 4
            => push => p1: 10
            <= pop  <= c1: 5
            <= pop  <= c1: 6
            => push => p1: 11
            <= pop  <= c1: 7
            => push => p1: 12
            <= pop  <= c1: 8
            => push => p1: 13
            <= pop  <= c1: 9
            => push => p1: 14
            <= pop  <= c1: 10
            => push => p1: 15
            <= pop  <= c1: 11
            => push => p1: 16
            <= pop  <= c1: 12
            => push => p1: 17
            <= pop  <= c1: 13
            <= pop  <= c1: 14
            <= pop  <= c1: 15
            <= pop  <= c1: 16
            => push => p1: 18
            <= pop  <= c1: 17
            => push => p1: 19
            <= pop  <= c1: 18
            => push => p1: 20
            <= pop  <= c1: 19
            <= pop  <= c1: 20
            +++++ EXIT!(p1) +++++
     */
}
