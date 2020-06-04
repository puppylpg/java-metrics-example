package example.concurrency.future.listenablefuture;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 在上一个ListenableFuture的基础上衔接一个动作（可同步可异步），返回动作串联后的值。
 *
 * @author puppylpg on 2020/06/04
 */
public class ListenableFutureChainDemo {

    public static void main(String... args) throws InterruptedException {
        // exiting thread pool
        ListeningExecutorService service = MoreExecutors.listeningDecorator(
                MoreExecutors.getExitingExecutorService(
                        (ThreadPoolExecutor) Executors.newFixedThreadPool(10),
                        10, TimeUnit.SECONDS
                )
        );

        // run task
        ListenableFuture<String> hello = service.submit(
                () -> {
                    Thread.sleep(1 * 1000);
                    System.out.println("Task: I say hello");
                    return "Hello";
                });

        // 把ListenableFuture（具体实现类必须实现了Runnable接口）作为回调
        ListenableFutureTask<String> world = ListenableFutureTask.create(
                () -> {
                    Thread.sleep(1 * 1000);
                    System.out.println("ListenableFuture as a callback: I say world");
                    return "World";
                });

        hello.addListener(world, service);

        ListenableFuture<String> helloWorld = Futures.transform(hello, new Function<String, String>() {
            @Nullable
            @Override
            public String apply(@Nullable String input) {
                return input + "World";
            }
        }, service);

        // 这个实现并不够异步
//        AsyncFunction<St|ring, String> f = input -> Futures.immediateFuture(input + "!!!!!");
        // 这个AsyncFunction的实现才是真正的异步
        AsyncFunction<String, String> f = input -> service.submit(
                () -> {
                    Thread.sleep(1 * 1000);
                    return input + "!!!!!";
                });

        ListenableFuture<String> helloWorld2 = Futures.transformAsync(helloWorld, f, service);


        // add callback for this task
        Futures.addCallback(
                // this task
                helloWorld2,
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

        Thread.currentThread().join(3000);
    }
}
