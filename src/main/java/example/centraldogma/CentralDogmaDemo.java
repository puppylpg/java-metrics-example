package example.centraldogma;

import com.linecorp.centraldogma.client.CentralDogma;
import com.linecorp.centraldogma.client.armeria.legacy.LegacyCentralDogmaBuilder;
import com.linecorp.centraldogma.common.Entry;
import com.linecorp.centraldogma.common.Query;
import com.linecorp.centraldogma.common.Revision;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;

/**
 * @author liuhaibo on 2018/12/19
 */
public class CentralDogmaDemo {

    public static void main(String... args) throws UnknownHostException {
        CentralDogma dogma = new LegacyCentralDogmaBuilder()
                .host("centraldogma1.inner.youdao.com", 80)
                .host("centraldogma2.inner.youdao.com", 80)
                .host("centraldogma3.inner.youdao.com", 80)
                .build();

        CompletableFuture<Entry<String>> future = dogma.getFile("bid-server", "e-greedy", Revision.HEAD, Query.ofText("/request-flow-ratio"));
        Entry<String> entry = future.join();
        System.out.println(entry.contentAsText());
    }
}
