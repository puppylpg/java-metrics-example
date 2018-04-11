package example.future;

import java.util.Random;
import java.util.concurrent.*;

public class FutureTaskDemoAndExecutorSubmit {

    /**
     * 使用{@link ExecutorService#submit(Runnable)}直接提交{@link Runnable}任务，而非{@link FutureTask}任务；
     * 从{@link ExecutorService#submit(Runnable)}的返回值{@link Future}对象中取结果；
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // 初始化一个Callable对象和FutureTask对象
//        Callable pAccount = new example.future.SparkTask();
//        FutureTask futureTask = new FutureTask(new Callable() {
//            Integer answer;
//            @Override
//            public Object call() throws Exception {
//                Thread.sleep(5000);
//                answer = new Integer(new Random().nextInt(10000));
//                System.out.println("您当前有" + answer + "在您的私有账户中");
//                return answer;
//            }
//        });
        // 使用futureTask创建一个线程
//        Thread pAccountThread = new Thread(futureTask);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        System.out.println("futureTask线程现在开始启动，启动时间为：" + System.nanoTime());
//        pAccountThread.start();
//        executorService.execute(futureTask);
        Future future = executorService.submit(new Callable<Integer>() {        // 用不用泛型皆可
            private Integer answer;

            @Override
            public Integer call() throws Exception {
                Thread.sleep(5000);
                answer = new Random().nextInt(10000);
                System.out.println("答案是：" + answer);
                return answer;
            }
        });

        System.out.println("主线程开始执行其他任务");

        // 测试后台的计算线程是否完成，如果未完成则等待
//        while (!futureTask.isDone()) {
        while (!future.isDone()) {
            Thread.sleep(500);
            System.out.println("还没有结果...");
        }
        System.out.println("结果出来啦，此时时间为" + System.nanoTime());
        int answer = 0;
        try {
//            answer = futureTask.get();
            answer = (int) future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("结果是：" + answer);
    }
}
