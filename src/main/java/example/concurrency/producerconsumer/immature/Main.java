package example.concurrency.producerconsumer.immature;

/**
 * @author puppylpg on 2018/11/29
 */
public class Main {

    public static void main(String... args) {
        int upperBound = 10;

        SleepyBoundedBuffer<Integer> buffer = new SleepyBoundedBuffer<>(3);

        Thread producer = new Thread(() -> {
            for (int i = 0; i < upperBound; i++) {
                try {
                    buffer.put(i);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        Thread consumer1 = new Thread(() -> {
            while (true) {
                try {
                    int i = buffer.take();
                    if (i >= upperBound - 1) {
                        break;
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        Thread consumer2 = new Thread(() -> {
            while (true) {
                try {
                    int i = buffer.take();
                    if (i >= upperBound - 1) {
                        break;
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        consumer1.start();
        consumer2.start();
        producer.start();
    }
}
