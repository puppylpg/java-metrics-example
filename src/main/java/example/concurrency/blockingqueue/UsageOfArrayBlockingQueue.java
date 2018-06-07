package example.concurrency.blockingqueue;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author liuhaibo on 2017/11/28
 */
public class UsageOfArrayBlockingQueue {

    // put() & take will throw InterruptedException if interrupted while waiting
    public static void main(String [] args) throws InterruptedException {
        final int POOL_SIZE = 10;
        Random r = new Random();
        ArrayBlockingQueue<Snooze> queue = new ArrayBlockingQueue<>(POOL_SIZE);

        for (int i = 0; i < POOL_SIZE; ++i) {
            queue.put(new Snooze(r.nextInt(1000)));
        }

        // in order of putting sequence
        while (!queue.isEmpty()) {
            System.out.println(queue.take().showName());
        }
    }

    private static class Snooze {
        long sleep;

        Snooze(long ms) throws InterruptedException {
            this.sleep = ms;
            synchronized (this) {
                wait(ms);
            }

            System.out.println(this);
        }

        public String showName() {
            return "Time: " + sleep;
        }

        @Override
        public String toString() {
            return Snooze.class.getSimpleName() + "===>" + sleep;
        }
    }
}