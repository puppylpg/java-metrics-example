package example.concurrency.synchronization.latch;

import java.util.concurrent.CountDownLatch;

public class Player implements Runnable {
    private CountDownLatch startSignal, finishSignal, playerReadySignal;
    private int num;

    private Board board;

    Player(CountDownLatch startSignal, CountDownLatch finishSignal, CountDownLatch playerReadySignal, int num, Board board) {
        this.startSignal = startSignal;
        this.finishSignal = finishSignal;
        this.playerReadySignal = playerReadySignal;
        this.num = num;
        this.board = board;
    }

    @Override
    public void run() {
        warmUp();
        readyAndWait();
        // 发令枪响后
        compete();
    }

    private void warmUp() {
        System.out.println("Player " + num + " finished warming up.");
    }

    /**
     * 自己准备好，并等待所有人都准备好
     */
    private void readyAndWait() {
        System.out.printf("Player %d is ready.%n", num);
        // 我准备好了
        playerReadySignal.countDown();
        try {
            // 等裁判信号
            startSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void compete() {
        System.out.println("Player " + num + " finished running.");
        // 我完成了
        finishSignal.countDown();
        board.setWinnerNumber(num);
    }
}
