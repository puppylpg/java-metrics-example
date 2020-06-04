package example.concurrency.future.listenablefuture;

import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 等待一堆ListenableFuture执行完之后再执行其他动作。
 *
 * @author puppylpg on 2020/06/04
 */
public class AsyncResultCollector {

    public static void main(String... args) throws InterruptedException, ExecutionException {
        // exiting thread pool
        ListeningExecutorService service = MoreExecutors.listeningDecorator(
                MoreExecutors.getExitingExecutorService(
                        (ThreadPoolExecutor) Executors.newFixedThreadPool(10),
                        10, TimeUnit.SECONDS
                )
        );

        ListenableFuture<String> hello = Futures.immediateFuture("Hello");
        // ListenableFutureTask.create创建出来的ListenableFuture必须想办法调用.....要不然不会执行啊
        // 不能跟人家immediate future比，人家又不需要执行......
        ListenableFutureTask<String> world = ListenableFutureTask.create(() -> "World");
        hello.addListener(world, service);

        ListenableFuture<String> symbol = service.submit(() -> {
            Thread.sleep(1000);
            return "!!!!!";
        });

        ListenableFuture<List<String>> allResults = Futures.allAsList(hello, world, symbol);

        // add callback for this task
        Futures.addCallback(
                // this task
                allResults,
                // callback for this task
                new FutureCallback<List<String>>() {
                    @Override
                    public void onSuccess(List<String> results) {
                        System.out.println("Use callback to collect the result: " + String.join("-", results));
                    }
                    @Override
                    public void onFailure(Throwable thrown) {
                        System.out.println(thrown.getMessage()); // escaped the explosion!
                    }
                },
                // use this pool to run callback
                service);

        // main thread wait at least 3s to exit
        // mainly to wait callback to be added into exiting thread pool
        Thread.currentThread().join(3000);
    }
}
