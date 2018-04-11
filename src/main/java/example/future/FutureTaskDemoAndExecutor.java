package example.future;

import java.util.Random;
import java.util.concurrent.*;

public class FutureTaskDemoAndExecutor {

    /**
     * 使用匿名内部类代替原来的SparkTask，但依然是使用{@link FutureTask}创建任务；
     * 使用{@link ExecutorService#execute(Runnable)}代替{@link Thread}执行任务；
     * 使用{@link FutureTask#get()}获取结果；
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // 初始化一个Callable对象和FutureTask对象
//        Callable sparkTask = new example.future.SparkTask();
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {        // 用不用泛型皆可
            private Integer answer;

            @Override
            public Integer call() throws Exception {
                Thread.sleep(5000);
                answer = new Random().nextInt(10000);
                System.out.println("答案是：" + answer);
                return answer;
            }
        });

        // 使用futureTask创建一个线程
//        Thread thread = new Thread(futureTask);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        System.out.println("futureTask线程现在开始启动，启动时间为：" + System.nanoTime());
//        thread.start();
        executorService.execute(futureTask);
        System.out.println("主线程开始执行其他任务");

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
