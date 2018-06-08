package example.concurrency.synchronization.latch;

import java.util.concurrent.CountDownLatch;

/**
 * 关键是要分清“谁”能操作哪个“信号量”，而“谁”又被哪个“信号量”卡着：
 * 运动员等裁判的发令枪，所以都被start.await()卡着，而start.countDown()由裁判操作；
 * 裁判等所有的运动员都结束才关闭比赛，所以被finish.await()卡着，而finish.countDown()由每个运动员操作。
 *
 * @author liuhaibo on 2018/06/08
 */
public class Referee {

    public static void main(String... args) {
        final int PLAYER_NUM = 5;
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch closeGate = new CountDownLatch(PLAYER_NUM);

        for(int i = 0; i < PLAYER_NUM; i++) {
            new Thread(new Player(startGate, closeGate, i)).start();
        }

        // 裁判等两秒，让他们把该做的热身工作做完
        try {
            Thread.sleep(1 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("1s past! Everyone is ready ~ Game start!!!");
        // 发令枪响，所有被卡的线程开始继续运行
        startGate.countDown();

        try {
            // 等待所有运动员结束
            closeGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Today's competition has FINISHED!");
    }

    private static class Player implements Runnable {
        CountDownLatch startSignal, finishSignal;
        int num;

        Player(CountDownLatch startSignal, CountDownLatch finishSignal, int num) {
            this.startSignal = startSignal;
            this.finishSignal = finishSignal;
            this.num = num;
        }

        @Override
        public void run() {
            warmUp();
            try {
                // 每个运动员都在等裁判的发令枪
                startSignal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 发令枪响后
            compete();
            // 每个运动员完成后，结束信号就-1
            finishSignal.countDown();
        }

        private void warmUp() {
            System.out.println("Player " + num + " finished warm up.");
        }

        private void compete() {
            System.out.println("Player " + num + " ended.");
        }
    }
}
