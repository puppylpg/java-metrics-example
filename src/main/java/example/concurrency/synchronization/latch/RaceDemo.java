package example.concurrency.synchronization.latch;

import java.util.concurrent.CountDownLatch;

/**
 * Latch: 等待启动一组相关操作，或者等待关闭一组相关操作
 *
 * Referee等所有运动员就绪；
 * Player等裁判开闸；
 * Referee等所有人结束，然后宣布结束。
 *
 * 所以准备了三个CountDownLatch。
 *
 * @author liuhaibo on 2018/06/08
 */
public class RaceDemo {

    private final static int PLAYER_NUM = 5;
    private static CountDownLatch playerReady = new CountDownLatch(PLAYER_NUM);
    private static CountDownLatch startGate = new CountDownLatch(1);
    private static CountDownLatch closeGate = new CountDownLatch(PLAYER_NUM);

    private static Board board = new Board();

    /**
     * Player 4 finished warming up.
     * Player 0 finished warming up.
     * Player 0 is ready.
     * Player 4 is ready.
     * Player 2 finished warming up.
     * Player 2 is ready.
     * Player 1 finished warming up.
     * Player 1 is ready.
     * Player 3 finished warming up.
     * Player 3 is ready.
     * Everyone is ready ~ Game start!!!
     * Player 1 finished running.
     * Player 2 finished running.
     * Player 0 finished running.
     * Player 3 finished running.
     * Player 4 finished running.
     * Today's competition has FINISHED! The winner is 2
     */
    public static void main(String... args) throws InterruptedException {

        for(int i = 0; i < PLAYER_NUM; i++) {
            new Thread(new Player(startGate, closeGate, playerReady, i, board)).start();
        }

        Referee referee = new Referee(startGate, closeGate, playerReady, board);

        referee.announceStart();
        referee.waitAllPlayerFinish();
        referee.announceFinish();
    }
}
