package example.concurrency.blockingqueue;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author liuhaibo on 2017/11/28
 */
public class UsageOfDelayQueue {
    private static final int STUDENT_NUM = 10;

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        Random r = new Random();
        DelayQueue<Student> students = new DelayQueue<>();
        // to run Student. Thread pool is useful only if the task needs long time to execute.
        ExecutorService service = Executors.newFixedThreadPool(STUDENT_NUM);
//        ExecutorService service = Executors.newSingleThreadExecutor();

        for (int i = 0; i < STUDENT_NUM; i++) {
            long costTime = 2000 + r.nextInt(3000);
            students.put(new Student("Student" + i, costTime));
            System.out.println("Student" + i + ": " + costTime);
        }

        // start take object from delayQueue
        // in order of `getDelay()`
        while (!students.isEmpty()) {
            // take a Student and execute since it's Runnable
            service.execute(students.take());
        }

        service.shutdown();

        // total time count
        service.awaitTermination(1, TimeUnit.HOURS);
        long stop = System.currentTimeMillis();
        System.out.println("Total time used: " + (stop - start) + "ms.");
    }

    /**
     * DelayQueue的元素必须实现{@link Delayed}接口（同时也实现了{@link Comparable}）接口
     * {@link Delayed#getDelay(TimeUnit)}决定了元素是否可以被取出
     * {@link Comparable#compareTo(Object)}决定了元素在队列中的排列顺序，当很多元素都可以被取出的时候，就按这个顺序进行
     */
    private static class Student implements Runnable, Delayed {
        private String name;
        // duration to do homework, assigned by Random
        private long homeworkDuration;
        // time the student can play
        private long playTime;

        Student(String name, long homeworkDuration) {
            this.name = name;
            this.homeworkDuration = homeworkDuration;
            playTime = homeworkDuration + System.currentTimeMillis();
        }

        @Override
        public void run() {
            System.out.println(name + " finished. Time used: " + homeworkDuration + "ms");

            synchronized (this) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(name + " done~");
        }

        /**
         * to judge the time after which object can be take out of the delay queue
         * @param unit
         * @return
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return (playTime - System.currentTimeMillis());
        }

        /**
         * to sort object in the delay queue
         * @param o
         * @return
         */
        @Override
        public int compareTo(Delayed o) {
            Student other = (Student) o;
            return homeworkDuration >= other.homeworkDuration ? 1 : -1;
            // IMPORTANT: show the meaning of compareTo()
//            return name.compareTo(other.name);
        }

    }
}