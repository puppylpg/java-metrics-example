package example.centraldogma;

import com.linecorp.centraldogma.client.CentralDogma;
import com.linecorp.centraldogma.client.Latest;
import com.linecorp.centraldogma.client.Watcher;
import com.linecorp.centraldogma.client.armeria.legacy.LegacyCentralDogmaBuilder;
import com.linecorp.centraldogma.common.Query;
import com.linecorp.centraldogma.common.Revision;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;

@Slf4j
public class CentralDogmaClient {

    //    private List<String> centralDogmaHosts = Arrays.asList("centraldogma1.inner.youdao.com", "centraldogma2.inner.youdao.com", "centraldogma3.inner.youdao.com");
    private List<String> centralDogmaHosts = Arrays.asList("nc107x.corp.youdao.com", "nc110x.corp.youdao.com", "nc090x.corp.youdao.com");

    private int centralDogmaPort = 36462;

    @Getter
    private CentralDogma dogmaClient;

    private static int INITIAL_VALUE_WAITING_TIME_SECONDS = 10;

    /**
     * 初始化centraldogma
     */
    public void initDogma() {
        try {
            LegacyCentralDogmaBuilder dogmaBuilder = new LegacyCentralDogmaBuilder();
            for (String host : centralDogmaHosts) {
                dogmaBuilder.host(host, centralDogmaPort);
                log.info("registered centraldogma host {" + host + ":" + centralDogmaPort + "}");
            }
            dogmaClient = dogmaBuilder.build();
        } catch (Exception e) {
            log.error("error initialize centraldogma client!!", e);
            System.exit(1);
        }
    }

    /**
     * 有就设置，没有file就没反应，也不报错……wtf……
     */
    public void watchConfigWithoutInitial(String projectName, String repoName,
                                          String fileName, BiConsumer<Revision, String> call) {
        try {
            if (dogmaClient != null) {
                Watcher<String> watcher = dogmaClient.fileWatcher(projectName, repoName, Query.ofText(fileName));
                watcher.watch(call);
            }
        } catch (Exception e) {
            log.error("centraldogma error register file watcher!", e);
        }
    }

    /**
     * 无限等待初始值，并监听文件变化，变化时执行回调函数。
     * <p>
     * 注意：该方法会无限等待初始值。如果监听的文件不存在，会陷入无限等待。
     *
     * @param projectName 配置所在项目
     * @param repoName    配置所在的仓库
     * @param fileName    配置的文件名
     * @param call        配置更新时执行的回调函数
     */
    public void watchConfigWaitingInitialInfinite(String projectName, String repoName,
                                                  String fileName, BiConsumer<Revision, String> call) {
        try {
            if (dogmaClient != null) {
                Watcher<String> watcher = dogmaClient.fileWatcher(projectName, repoName, Query.ofText(fileName));
                watcher.watch(call);
                watcher.awaitInitialValue();
            }
        } catch (Exception e) {
            log.error("centraldogma error register file watcher!", e);
        }
    }

    /**
     * 有限时间内等待初始值，并监听文件变化，变化时执行回调函数。
     * <p>
     * 启动时，等待初始值{@link #INITIAL_VALUE_WAITING_TIME_SECONDS}，超时可能是服务挂了或者节点不存在，
     * 将异常交给调用者，调用者可以选择fast fail。
     *
     * @param projectName 配置所在项目
     * @param repoName    配置所在的仓库
     * @param fileName    配置的文件名
     * @param call        配置更新时执行的回调函数
     * @throws TimeoutException     等待初始值超时
     * @throws InterruptedException 线程等待初始值时被中断
     */
    public void watchConfigWaitingInitialLimited(String projectName, String repoName,
                                                 String fileName, BiConsumer<Revision, String> call)
            throws TimeoutException, InterruptedException {
        if (dogmaClient != null) {
            Watcher<String> watcher = dogmaClient.fileWatcher(projectName, repoName, Query.ofText(fileName));
            watcher.watch(call);
            watcher.awaitInitialValue(INITIAL_VALUE_WAITING_TIME_SECONDS, TimeUnit.SECONDS);
        }
    }


    /**
     * 只在一开始获取初始值，之后变了就不通知了。。。
     */
    public void watchConfigJustInitialValue(String projectName, String repoName, String fileName) {
        try {
            if (dogmaClient != null) {
                Watcher<String> watcher = dogmaClient.fileWatcher(projectName, repoName, Query.ofText(fileName));
                Latest<String> latest = watcher.awaitInitialValue();
                System.out.println(latest.value());
            }
        } catch (Exception e) {
            log.error("centraldogma error register file watcher!", e);
        }
    }

}
