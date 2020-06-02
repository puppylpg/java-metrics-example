package example.concurrency.future.listenablefuture;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author puppylpg on 2020/06/03
 */
public class ListenableFutureDemo {

    public static void main(String... args) throws InterruptedException {
        // exiting thread pool
        ListeningExecutorService service = MoreExecutors.listeningDecorator(
                MoreExecutors.getExitingExecutorService(
                        (ThreadPoolExecutor) Executors.newFixedThreadPool(10),
                        10, TimeUnit.SECONDS
                )
        );

        // new a listening pool with current thread pool
//        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        // run task
        ListenableFuture<String> explosion = service.submit(
                () -> {
                    Thread.sleep(1 * 1000);
                    System.out.println("Task: I say hello world");
                    return "Hello world!";
                });

        // add callback for this task
        Futures.addCallback(
                // this task
                explosion,
                // callback for this task
                new FutureCallback<String>() {
                    @Override
                    public void onSuccess(String explosion) {
                        System.out.println("Call back: " + explosion);
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
