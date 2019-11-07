package example.centraldogma.migrage;

import com.linecorp.centraldogma.client.CentralDogma;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Set;

/**
 * 注意事项见{@link ProjectSync}。
 *
 * @author liuhaibo on 2019/11/07
 */
public class RepoSync extends Sync {

    RepoSync(CentralDogma readDogma, CentralDogma writeDogma) {
        super(readDogma, writeDogma);
    }

    @SuppressWarnings("unchecked")
    public void syncReposForProject(Set<String> targetRepos, Set<String> currentRepos, Set<String> alreadyRemovedRepos, String project) throws InterruptedException {

        Collection<String> unExistentRepos = CollectionUtils.subtract(targetRepos, currentRepos);
        Collection<String> uselessRepos = CollectionUtils.subtract(currentRepos, targetRepos);

        removeRepos(uselessRepos, project);
        tryRestoreBeforeCreateRepos(unExistentRepos, alreadyRemovedRepos, project);

        System.out.println(String.format("%sWaiting %s ms for repos in project[%s] status set.", REPO_INDENT, SYNC_INTERVAL, project));
        Thread.sleep(SYNC_INTERVAL);
        System.out.println(String.format("%sDone.", REPO_INDENT));
    }

    private void tryRestoreBeforeCreateRepos(Collection<String> nonExistentRepos, Set<String> alreadyRemovedRepos, String project) {
        for (String nonExistentRepo : nonExistentRepos) {

            if (isReserved(nonExistentRepo)) {
                continue;
            }

            if (alreadyRemovedRepos.contains(nonExistentRepo)) {
                desDogma.unremoveRepository(project, nonExistentRepo);
                System.out.println(String.format("%sRepo[%s] in Project[%s] is restored from removed projects.", REPO_INDENT, nonExistentRepo, project));
            } else {
                desDogma.createRepository(project, nonExistentRepo);
                System.out.println(String.format("%sRepo[%s] in Project[%s] is newly created.", REPO_INDENT, nonExistentRepo, project));
            }
        }
    }

    private void removeRepos(Collection<String> uselessRepos, String project) {
        for (String uselessRepo : uselessRepos) {

            if (isReserved(uselessRepo)) {
                continue;
            }

            desDogma.removeRepository(project, uselessRepo);
            System.out.println(String.format("%sRepo[%s] in Project[%s] is removed.", REPO_INDENT, uselessRepo, uselessRepo));
        }
    }
}
