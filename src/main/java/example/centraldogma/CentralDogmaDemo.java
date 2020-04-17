package example.centraldogma;

import com.linecorp.centraldogma.client.CentralDogma;
import com.linecorp.centraldogma.common.Entry;
import com.linecorp.centraldogma.common.Query;
import com.linecorp.centraldogma.common.Revision;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

/**
 * @author liuhaibo on 2018/12/19
 */
public class CentralDogmaDemo {

    private static CentralDogmaClient clientWrapper = new CentralDogmaClient();
    private static CentralDogma dogma;

    static {
        clientWrapper.initDogma();
        dogma = clientWrapper.getDogmaClient();
    }

    public static void main(String... args) throws InterruptedException, TimeoutException {

//        getFile();

        watchFile();
    }

    private static void getFile() {
        CompletableFuture<Entry<String>> future = dogma.getFile("bid-server", "config", Revision.HEAD, Query.ofText("/e-greedy/flow-ratio"));
        Entry<String> entry = future.join();
        System.out.println(entry.contentAsText());
    }

    private static void watchFile() throws TimeoutException, InterruptedException {
//        // unknown repo
//        clientWrapper.watchConfigWaitingInitialLimited(
//                "unknown", "config", "/e_greedy/flow_ratio",
//                ((revision, s) -> System.out.print(s))
//        );

//        // unknown repo
//        clientWrapper.watchConfigWaitingInitialLimited(
//                "bid-server", "unknown", "/e_greedy/flow_ratio",
//                ((revision, s) -> System.out.print(s))
//        );

        // unknown file
        clientWrapper.watchConfigWithoutInitial(
                "bid-server", "config", "/unknown",
                ((revision, s) -> System.out.print(s))
        );

        clientWrapper.watchConfigWaitingInitialLimited(
                "bid-server", "config", "/e_greedy/flow_ratio",
                ((revision, s) -> System.out.print(s))
        );

        clientWrapper.watchConfigWaitingInitialLimited(
                "bid-server", "config", "/e_greedy/flow_ratio",
                ((revision, s) -> System.out.print(s))
        );

        // 本线程最多等这么久再死，或者如果在此之前不是alive状态了，直接退出。
        Thread.currentThread().join(100 * 1000);
        System.out.println("不等了不等了...");
    }
}
