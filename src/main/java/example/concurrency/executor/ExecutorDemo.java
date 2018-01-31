package example.concurrency.executor;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * {@link Executor} <- {@link ExecutorService} <- {@link AbstractExecutorService} <- {@link ThreadPoolExecutor}
 *
 * knows the relations among classes above;
 * learn to create a custom thread poll;
 * learn to not use {@link Executors}, unless you surely want a single/fixed pool;
 * learn to use {@link Future#get(long, TimeUnit)};
 * use a method with timeout anytime if exists;
 * learn methods in {@link ExecutorService};
 * learn to close a {@link ExecutorService} in recommended way;
 *
 * @author liuhaibo on 2017/12/19
 */
public class ExecutorDemo {

    private static Runnable taskRun = createRunnableTask();
    private static Callable<String> taskCall = createCallableTask("Westlife");
    private static List<Callable<String>> taskCalls = createCallableTasks();

    /**
     * namingPattern: use String.format() to set a pattern,
     *      replacing %d with a counter keeps the threads it has already created
     * daemon: when only daemon are left, JVM will quit
     */
    private static ThreadFactory myThreadFactory = new BasicThreadFactory.Builder()
            .namingPattern("liuhb's No.%d thread").daemon(true).build();

    /**
     * [corePoolSize, maximumPoolSize]:
     *      1. < corePoolSize: create thread even some are idle;
     *      2. corePoolSize < x < maximumPoolSize: create thread only none is idle;
     *      3. = maximumPoolSize: stop creating thread
     *
     *      if corePoolSize == maximumPoolSize, that's a fixed size pool.
     *
     * keepAliveTime: the maximum time that excess idle threads will wait for new tasks before terminating
     *
     * BlockingQueue<Runnable>: the queue to use for holding tasks before they are executed.
     *      This queue will hold **only the Runnable tasks** submitted by the execute method
     *
     * ThreadFactory: recommended!!! Using your own name for the thread is easy to understand
     *
     * RejectedExecutionHandler: what to do if task can't execute.
     *      {@link ThreadPoolExecutor.AbortPolicy#AbortPolicy()} just do nothing
     */
    private static ExecutorService namedPool =
            new ThreadPoolExecutor(
                    4,
                    8,
                    20L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    myThreadFactory,
                    new ThreadPoolExecutor.AbortPolicy());

    /**
     * this will use a default ThreadFactory, not recommended!
     */
    @Deprecated
    ExecutorService unnamedPool =
            new ThreadPoolExecutor(
                    1,
                    1,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());

    /**
     * all these created by {@link Executors} are not recommended:
     *      1. fixed/single pool: task queue may be too large to OOM;
     *      2. cached/scheduled pool: task numbers can be from 0 to {@link Integer#MAX_VALUE}, may cause OOM.
     */
    @Deprecated
    ExecutorService namedPool2 = Executors.newCachedThreadPool(myThreadFactory);
    @Deprecated
    ExecutorService namedSinglePool = Executors.newSingleThreadExecutor(myThreadFactory);
    @Deprecated
    ExecutorService namedFixedPool = Executors.newFixedThreadPool(2, myThreadFactory);
    @Deprecated
    ExecutorService namedScheduledPool = Executors.newScheduledThreadPool(2, myThreadFactory);

    private static long executeTime = 1000;
    private static long waitingTimeout = 5000;

    public static void main(String [] args) throws InterruptedException, ExecutionException, TimeoutException {

        // these methods are all in ExecutorService

        System.out.println("\nExecute a task:");
        executeTask();
        System.out.println("\nSubmit tasks:");
        submitTask();
        System.out.println("\ninvokeAny:");
        invokeAnyTask();
        System.out.println("\ninvokeAll:");
        invokeAllTask();

        shutdownPool();
    }

    private static void executeTask() {
        namedPool.execute(taskRun);
    }

    /**
     * always use {@link Future#get(long, TimeUnit)} to set a waitingTimeout
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    private static void submitTask() throws ExecutionException, InterruptedException, TimeoutException {
        Future<?> future = namedPool.submit(taskRun);
        System.out.println("FutureResult of taskRun: " + future.get(waitingTimeout, TimeUnit.MILLISECONDS));

        Future<String> futureResult = namedPool.submit(createCallableTask("Love Ya~"));
        System.out.println("FutureResult of single taskCall: " + futureResult.get(waitingTimeout, TimeUnit.MILLISECONDS));
    }

    /**
     * always use {@link ExecutorService#invokeAny(Collection, long, TimeUnit)} to set a waitingTimeout
     *
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    private static void invokeAnyTask() throws InterruptedException, ExecutionException, TimeoutException {
        String singleResult = namedPool.invokeAny(taskCalls, waitingTimeout, TimeUnit.MILLISECONDS);
        System.out.println("Result of single taskCall: " + singleResult);
    }

    /**
     * always use {@link ExecutorService#invokeAll(Collection, long, TimeUnit)} to set a waitingTimeout
     *
     * @throws InterruptedException
     */
    private static void invokeAllTask() throws InterruptedException, TimeoutException, ExecutionException {
        List<Future<String>> futureResults = namedPool.invokeAll(taskCalls, waitingTimeout, TimeUnit.MILLISECONDS);
        System.out.println("FutureResults of taskCalls: ");
        for(Future<String> future : futureResults) {
            String futureResult = future.get(waitingTimeout, TimeUnit.MILLISECONDS);
            System.out.println(futureResult);
        }
    }

    /**
     * recommended way to shutdown a pool!!!
     *
     * @return unfinished tasks
     */
    private static List<Runnable> shutdownPool() {
        // stop accepting new tasks
        namedPool.shutdown();
        try {
            if (!namedPool.awaitTermination(waitingTimeout, TimeUnit.MILLISECONDS)) {
                // return unfinished tasks before termination time
                return namedPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            // return unfinished tasks because of InterruptedException occurred in awaitTermination()
            return namedPool.shutdownNow();
        }
        // all tasks are finished before termination time, none left
        return null;
    }

    private static Callable<String> createCallableTask(String str) {
        return () -> {
            System.out.println("Do a callable job.");
            TimeUnit.MILLISECONDS.sleep(executeTime);
            System.out.println("In Callable: " + Thread.currentThread().getName());
            return str;
        };
    }

    private static Runnable createRunnableTask() {
        return () -> {
            System.out.println("Do a runnable job.");
            try {
                TimeUnit.MILLISECONDS.sleep(executeTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("In Runnable: " + Thread.currentThread().getName());
        };
    }

    private static List<Callable<String>> createCallableTasks() {
        return Arrays.asList(
                createCallableTask("Hello"),
                createCallableTask("World"),
                createCallableTask("Bye~")
        );
    }

}