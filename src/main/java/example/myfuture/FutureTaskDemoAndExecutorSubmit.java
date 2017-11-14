package example.myfuture;

import java.util.Random;
import java.util.concurrent.*;

@SuppressWarnings("all")
public class FutureTaskDemoAndExecutorSubmit {
    public static void main(String[] args) {
        // 初始化一个Callable对象和FutureTask对象
//        Callable pAccount = new example.myfuture.PrivateAccount();
//        FutureTask futureTask = new FutureTask(new Callable() {
//            Integer totalMoney;
//            @Override
//            public Object call() throws Exception {
//                Thread.sleep(5000);
//                totalMoney = new Integer(new Random().nextInt(10000));
//                System.out.println("您当前有" + totalMoney + "在您的私有账户中");
//                return totalMoney;
//            }
//        });
        // 使用futureTask创建一个线程
//        Thread pAccountThread = new Thread(futureTask);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        System.out.println("futureTask线程现在开始启动，启动时间为：" + System.nanoTime());
//        pAccountThread.start();
//        executorService.execute(futureTask);
        Future future = executorService.submit(new Callable<Integer>() {        // 用不用泛型皆可
            Integer totalMoney;
            @Override
            public Integer call() throws Exception {
                Thread.sleep(5000);
                totalMoney = new Integer(new Random().nextInt(10000));
                System.out.println("您当前有" + totalMoney + "在您的私有账户中");
                return totalMoney;
            }
        });
        System.out.println("主线程开始执行其他任务");
        // 从其他账户获取总金额
        int totalMoney = new Random().nextInt(100000);
        System.out.println("现在你在其他账户中的总金额为" + totalMoney);
        System.out.println("等待私有账户总金额统计完毕...");
        // 测试后台的计算线程是否完成，如果未完成则等待
//        while (!futureTask.isDone()) {
        while (!future.isDone()) {
            try {
                Thread.sleep(500);
                System.out.println("私有账户计算未完成继续等待...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("futureTask线程计算完毕，此时时间为" + System.nanoTime());
        Integer privateAccountMoney = null;
        try {
//            privateAccountMoney = (Integer) futureTask.get();
            privateAccountMoney = (Integer) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("您现在的总金额为：" + totalMoney + privateAccountMoney.intValue());
    }
}

/*
@SuppressWarnings("all")
class example.myfuture.PrivateAccount implements Callable {
    Integer totalMoney;

    @Override
    public Object call() throws Exception {
        Thread.sleep(5000);
        totalMoney = new Integer(new Random().nextInt(10000));
        System.out.println("您当前有" + totalMoney + "在您的私有账户中");
        return totalMoney;
    }

}*/
