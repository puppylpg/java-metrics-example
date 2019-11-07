package example.centraldogma.migrage;

import com.linecorp.centraldogma.client.CentralDogma;
import com.linecorp.centraldogma.client.armeria.legacy.LegacyCentralDogmaBuilder;
import com.linecorp.centraldogma.common.EntryType;
import com.linecorp.centraldogma.common.Revision;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * 从一个dogma提交数据到另一个dogma。
 * <p>
 * dogma使用服务本地的git repo + 远程zk同步。所以仅替换dogma的/data目录并不能达到数据替换的效果。
 * 这里用的方式是遍历dogma所有节点，增删（dogma使用标记删除法）project和repo，对文件内容使用git push重建，
 * 所以只能做到两个dogma HEAD版本数据完全同步，不能做到历史提交记录也完全一致。
 * <p>
 * 以后如果需要新部署dogma服务给bs用，可以使用该工具copy线上dogma的数据到新的dogma，不用再手动重建数据了。
 *
 * 用法：修改src dogma host/port和des dogma host/port，运行即可。
 *
 * @author liuhaibo on 2019/11/06
 */
public class DogmaDataMigrate {

    private static String SRC_DOGMA_HOST = "centraldogma2.inner.youdao.com";
    //    private static String SRC_DOGMA_HOST = "nc107x.corp.youdao.com";
    private static int SRC_DOGMA_PORT = 80;

    //    private static String DES_DOGMA_HOST = "nc107x.corp.youdao.com";
    private static String DES_DOGMA_HOST = "th077x.corp.youdao.com";
    private static int DES_DOGMA_PORT = 36462;

    private static CentralDogma srcDogma;

    private static CentralDogma desDogma;

    static {
        try {
            srcDogma = new LegacyCentralDogmaBuilder()
                    .host(SRC_DOGMA_HOST, SRC_DOGMA_PORT)
                    .build();
            desDogma = new LegacyCentralDogmaBuilder()
                    .host(DES_DOGMA_HOST, DES_DOGMA_PORT)
                    .build();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static ProjectSync projectSync = new ProjectSync(srcDogma, desDogma);
    private static RepoSync repoSync = new RepoSync(srcDogma, desDogma);
    private static FileSync fileSync = new FileSync(srcDogma, desDogma);


    public static void main(String... args) throws ExecutionException, InterruptedException {

        Set<String> targetProjects = srcDogma.listProjects().get();
        Set<String> currentProjects = desDogma.listProjects().get();
        Set<String> alreadyRemovedProjects = desDogma.listRemovedProjects().get();

        projectSync.syncProjects(targetProjects, currentProjects, alreadyRemovedProjects);

        for (String project : targetProjects) {

            Set<String> targetRepos = srcDogma.listRepositories(project).get().keySet();
            Set<String> currentRepos = desDogma.listRepositories(project).get().keySet();
            Set<String> alreadyRemovedRepos = desDogma.listRemovedRepositories(project).get();

            repoSync.syncReposForProject(targetRepos, currentRepos, alreadyRemovedRepos, project);

            for (String repo : targetRepos) {
                Map<String, EntryType> targetFiles = srcDogma.listFiles(project, repo, Revision.HEAD, "/**").get();
                Map<String, EntryType> currentFiles = desDogma.listFiles(project, repo, Revision.HEAD, "/**").get();

                fileSync.syncFiles(targetFiles, currentFiles, repo, project);
            }
        }
    }


}
