package example.concurrency.executor;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.*;

/**
 * 提交一大堆并行任务，然后使用{@link CompletionService}及时获取{@link Future}结果。
 * 避免了轮询。
 *
 * @author liuhaibo on 2018/06/13
 */
public class CompletionServiceDemo {

    private static final int TASK_NUM = 20;

    private static Callable<String> createCallableTask(String str) {
        return () -> {
            int time = RandomUtils.nextInt(10, 1000);
            TimeUnit.MILLISECONDS.sleep(time);
            System.out.println("Finished: " + Thread.currentThread().getName() + " time=" + time);
            return str;
        };
    }

    private static void doAnotherJob() throws InterruptedException {
        Thread.sleep(1000);
    }

    public static void main(String... args) throws InterruptedException {
        Executor executor = new ThreadPoolExecutor(
                4,
                8,
                20L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.AbortPolicy());

        CompletionService<String> getStringService = new ExecutorCompletionService<>(executor);

        // 提交一堆并行任务，最好是和CPU无关的，比如I/O密集型的下载任务
        for (int i = 0; i < TASK_NUM; i++) {
            getStringService.submit(createCallableTask("Hello => " + i));
        }

        // 同时并行做一些耗时的任务
        doAnotherJob();

        // 使用CompletionService，去BlockingQueue中取一些已完成的任务
        // 另一种低级实现方法：保留每个任务关联的Future，并反复调用future.get(timeout=0)去轮询，获取任务完成情况
        // 或者使用invokeAll，获取返回的List<Future<T>>，然后反复调用future.get(timeout=0)去轮询
        try {
            for (int i = 0; i < TASK_NUM; i++) {
                Future<String> future = getStringService.take();
                String s = future.get();
                System.out.println(s);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(executor);
    }

    /*
    Finished: pool-1-thread-1 time=27
    Finished: pool-1-thread-4 time=235
    Finished: pool-1-thread-3 time=345
    Finished: pool-1-thread-4 time=282
    Finished: pool-1-thread-2 time=770
    Finished: pool-1-thread-2 time=160
    Finished: pool-1-thread-1 time=948
    Hello => 0
    Hello => 3
    Hello => 2
    Hello => 5
    Hello => 1
    Hello => 8
    Hello => 4
    Finished: pool-1-thread-2 time=104
    Hello => 9
    Finished: pool-1-thread-3 time=740
    Hello => 6
    Finished: pool-1-thread-2 time=228
    Hello => 11
    Finished: pool-1-thread-4 time=890
    Hello => 7
    Finished: pool-1-thread-4 time=30
    Hello => 14
    Finished: pool-1-thread-2 time=189
    Hello => 13
    Finished: pool-1-thread-3 time=375
    Hello => 12
    Finished: pool-1-thread-2 time=228
    Hello => 16
    Finished: pool-1-thread-1 time=826
    Hello => 10
    Finished: pool-1-thread-3 time=446
    Hello => 17
    Finished: pool-1-thread-2 time=487
    Hello => 18
    Finished: pool-1-thread-4 time=757
    Hello => 15
    Finished: pool-1-thread-1 time=406
    Hello => 19
    java.util.concurrent.ThreadPoolExecutor@1f17ae12[Running, pool size = 4, active threads = 0, queued tasks = 0, completed tasks = 20]
     */
}
