package example.future;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo {

    /**
     * 使用{@link FutureTask}创建任务；
     * 使用{@link Thread}启动任务；
     * 使用{@link FutureTask#get()}获取结果；
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // 初始化一个Callable对象和FutureTask对象
        Callable<Integer> sparkTask = new SparkTask();
        FutureTask<Integer> futureTask = new FutureTask<>(sparkTask);

        // 使用futureTask创建一个线程
        Thread thread = new Thread(futureTask);
        System.out.println("futureTask线程现在开始启动，启动时间为：" + System.nanoTime());
        thread.start();

        System.out.println("主线程开始执行其他任务");
        System.out.println("等待futureTask给结果...");

        // 测试后台的计算线程是否完成，如果未完成则等待
        while (!futureTask.isDone()) {
                Thread.sleep(500);
                System.out.println("还没有结果...");
        }
        System.out.println("结果出来啦，此时时间为" + System.nanoTime());

        int answer = 0;
        try {
            answer = futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("结果是：" + answer);
    }
}

/**
 * 模拟一个耗时的spark任务
 */
class SparkTask implements Callable<Integer> {
    private Integer answer;

    @Override
    public Integer call() throws Exception {
        Thread.sleep(5000);
        answer = new Random().nextInt(10000);
        System.out.println("答案是：" + answer);
        return answer;
    }
}
