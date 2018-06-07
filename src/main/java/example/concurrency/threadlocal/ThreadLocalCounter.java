package example.concurrency.threadlocal;

/**
 * @author liuhaibo on 2017/11/21
 */
public class ThreadLocalCounter {

    /**
     * This is indeed a map: k-v is <Thread, Integer>
     * Specify initial value: ThreadLocal.withInitial(Supplier)
     */
    private static ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

    /**
     * get the next value
     * @return the next value
     */
    public int getNextNum() {
        counter.set(counter.get() + 1);
        try {
            // There'll be IllegalMonitorStateException if it's not synchronized
            // A thread which wanna use a obj's wait/notify/notifyAll must have the obj's lock
            synchronized (this) {
                wait(5);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return counter.get();
    }

    public static void main(String[] args) {
        ThreadLocalCounter threadLocalCounter = new ThreadLocalCounter();
        // 3 threads use 1 counter, but get their numbers separately
        TestClient t1 = new TestClient(threadLocalCounter);
        TestClient t2 = new TestClient(threadLocalCounter);
        TestClient t3 = new TestClient(threadLocalCounter);
        t1.start();
        t2.start();
        t3.start();
    }

    private static class TestClient extends Thread {
        private ThreadLocalCounter tlCounter;

        public TestClient(ThreadLocalCounter tlCounter) {
            this.tlCounter = tlCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                // print 5 numbers for every thread
                System.out.println("thread[" + Thread.currentThread().getName() + "] --> counter["
                        + tlCounter.getNextNum() + "]");
            }
        }
    }
}