package example.concurrency.produconsu.manually;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author liuhaibo on 2018/4/10
 */
public class ProducerConsumer {

    public static void main(String [] args) {
        // 在Java中，ArrayDeque/LinkedList实现了队列的接口
//        Queue<Integer> queue = new LinkedList<>();
        Queue<Integer> queue = new ArrayDeque<>();
        int maxSize = 5, totalNum = 10;
        Producer producer = new Producer(queue, maxSize, "p1", totalNum);
        Consumer consumer1 = new Consumer(queue, "c1");
        Consumer consumer2 = new Consumer(queue, "c2");
        consumer1.start();
        consumer2.start();
        producer.start();
    }

    /*

    c2 start at 1523380205137
    ----- I get the lock~(c2) -----
    c1 start at 1523380205143
    ----- No elements to consume, release lock, waiting(c2) -----
    ----- I get the lock~(c1) -----
    ----- No elements to consume, release lock, waiting(c1) -----
    p1 start at 1523380205148
    +++++ I get the lock~(p1) +++++
    => push => p1: 1
    +++++ Hey, get up!(p1) +++++
    +++++ I am gonna release the lock~(p1) +++++
    ----- I am awake and get the lock(c1) -----
    <= pop  <= c1: 1
    ----- Hey, get up!(c1) -----
    ----- I am gonna release the lock~(c1) -----
    ----- I get the lock~(c1) -----
    ----- No elements to consume, release lock, waiting(c1) -----
    ----- I am awake and get the lock(c2) -----
    ----- No elements to consume, release lock, waiting(c2) -----
    +++++ I get the lock~(p1) +++++
    => push => p1: 2
    +++++ Hey, get up!(p1) +++++
    +++++ I am gonna release the lock~(p1) +++++
    +++++ I get the lock~(p1) +++++
    => push => p1: 3
    +++++ Hey, get up!(p1) +++++
    +++++ I am gonna release the lock~(p1) +++++
    +++++ I get the lock~(p1) +++++
    => push => p1: 4
    +++++ Hey, get up!(p1) +++++
    +++++ I am gonna release the lock~(p1) +++++
    +++++ I get the lock~(p1) +++++
    => push => p1: 5
    +++++ Hey, get up!(p1) +++++
    +++++ I am gonna release the lock~(p1) +++++
    +++++ I get the lock~(p1) +++++
    => push => p1: 6
    +++++ Hey, get up!(p1) +++++
    +++++ I am gonna release the lock~(p1) +++++
    +++++ I get the lock~(p1) +++++
    +++++ No space to produce, release lock, waiting(p1) +++++
    ----- I am awake and get the lock(c2) -----
    <= pop  <= c2: 2
    ----- Hey, get up!(c2) -----
    ----- I am gonna release the lock~(c2) -----
    ----- I get the lock~(c2) -----
    <= pop  <= c2: 3
    ----- Hey, get up!(c2) -----
    ----- I am gonna release the lock~(c2) -----
    ----- I get the lock~(c2) -----
    <= pop  <= c2: 4
    ----- Hey, get up!(c2) -----
    ----- I am gonna release the lock~(c2) -----
    ----- I get the lock~(c2) -----
    <= pop  <= c2: 5
    ----- Hey, get up!(c2) -----
    ----- I am gonna release the lock~(c2) -----
    ----- I get the lock~(c2) -----
    <= pop  <= c2: 6
    ----- Hey, get up!(c2) -----
    ----- I am gonna release the lock~(c2) -----
    ----- I get the lock~(c2) -----
    ----- No elements to consume, release lock, waiting(c2) -----
    ----- I am awake and get the lock(c1) -----
    ----- No elements to consume, release lock, waiting(c1) -----
    +++++ I am awake and get the lock(p1) +++++
    => push => p1: 7
    +++++ Hey, get up!(p1) +++++
    +++++ I am gonna release the lock~(p1) +++++
    ----- I am awake and get the lock(c1) -----
    <= pop  <= c1: 7
    ----- Hey, get up!(c1) -----
    ----- I am gonna release the lock~(c1) -----
    ----- I get the lock~(c1) -----
    ----- No elements to consume, release lock, waiting(c1) -----
    +++++ I get the lock~(p1) +++++
    => push => p1: 8
    +++++ Hey, get up!(p1) +++++
    +++++ I am gonna release the lock~(p1) +++++
    +++++ I get the lock~(p1) +++++
    => push => p1: 9
    +++++ Hey, get up!(p1) +++++
    +++++ I am gonna release the lock~(p1) +++++
    +++++ I get the lock~(p1) +++++
    => push => p1: 10
    +++++ Hey, get up!(p1) +++++
    +++++ I am gonna release the lock~(p1) +++++
    ----- I am awake and get the lock(c2) -----
    <= pop  <= c2: 8
    ----- Hey, get up!(c2) -----
    ----- I am gonna release the lock~(c2) -----
    ----- I get the lock~(c2) -----
    <= pop  <= c2: 9
    ----- Hey, get up!(c2) -----
    ----- I am gonna release the lock~(c2) -----
    ----- I get the lock~(c2) -----
    <= pop  <= c2: 10
    ----- Hey, get up!(c2) -----
    ----- I am gonna release the lock~(c2) -----
    ----- I get the lock~(c2) -----
    ----- No elements to consume, release lock, waiting(c2) -----

    // c2 wait()，释放了锁，p1也不抢了，自然落到了c1手中，然后c1也发现临界资源不满足条件，也wait()，再也没人唤醒他们了

    ----- I am awake and get the lock(c1) -----
    ----- No elements to consume, release lock, waiting(c1) -----
    +++++ EXIT!(p1) +++++

    */
}
