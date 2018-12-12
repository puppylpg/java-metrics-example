package example.concurrency.executor;

import java.util.concurrent.Executor;

/**
 * @author liuhaibo on 2018/11/14
 */
public class WithinThreadExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        command.run();
    }

    public static void main(String... args) {
        new Thread(
                () -> {
                    while (true) {
                        // 有人想让我退出？行吧我退了
                        if (Thread.interrupted()) {
                            break;
                        }
                        // Continue to do nothing
                    }
                }
        ).start();

    }
}
