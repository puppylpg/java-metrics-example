package example.thread.interrupt;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * @author liuhaibo on 2018/11/15
 */
public class RevokablePrimeProducer extends Thread {

    private final BlockingQueue<BigInteger> queue;

    RevokablePrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted()) {
                queue.put(p = p.nextProbablePrime());
            }
        } catch (InterruptedException e) {
            // do something
        }
    }

    /**
     * User system signal to cancel
     */
    public void cancel() {
        interrupt();
    }
}
