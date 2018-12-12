package example.concurrency.executor;

import java.util.concurrent.Executor;

/**
 * @author liuhaibo on 2018/11/14
 */
public class ThreadPerTaskExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
