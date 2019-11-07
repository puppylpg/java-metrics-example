package example.centraldogma.migrage;

import com.linecorp.centraldogma.client.CentralDogma;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * @author liuhaibo on 2019/11/07
 */
public class Sync {

    protected static long SYNC_INTERVAL = TimeUnit.SECONDS.toMillis(3);

    private static String DOGMA = "dogma";

    private static String META = "meta";

    protected static String INDENT = "    ";
    protected static String PROJECT_INDENT = "";
    protected static String REPO_INDENT = INDENT + INDENT;
    protected static String FILE_INDENT = INDENT + INDENT + INDENT;

    @Setter
    protected CentralDogma srcDogma;

    @Setter
    protected CentralDogma desDogma;

    Sync(CentralDogma srcDogma, CentralDogma desDogma) {
        this.srcDogma = srcDogma;
        this.desDogma = desDogma;
    }

    protected boolean isReserved(String name) {
        return DOGMA.equals(name) || META.equals(name);
    }

}
