package example.concurrency.synchronization.latch;

import java.util.concurrent.atomic.AtomicInteger;

public class Board {

    private AtomicInteger winnerNumber = new AtomicInteger(-1);

    public boolean setWinnerNumber(int number) {
        // 如果还没人设置过，那我就是第一个，我就是冠军
        return winnerNumber.compareAndSet(-1, number);
    }

    public int getWinnerNumber() {
        return winnerNumber.get();
    }
}
