package example.centraldogma.migrage;

import com.linecorp.centraldogma.client.CentralDogma;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Set;

/**
 * Project分两种：现存的和删除的。dogma使用标记删除，所以删除的project可恢复。
 * 但是新建project既不能和现存的同名，也不能和已删除的同名。如果需要新建已删除过的同名project，只能从已删除的project中恢复。
 * <p>
 * 实际使用发现，创建完project需要少许事件等待同步系统同步，然后才能增删改查该project的数据。所以需要sleep一小会儿。
 * <p>
 * Repository也存在上述同样的问题。
 *
 * @author liuhaibo on 2019/11/07
 */
public class ProjectSync extends Sync {

    ProjectSync(CentralDogma readDogma, CentralDogma writeDogma) {
        super(readDogma, writeDogma);
    }

    @SuppressWarnings("unchecked")
    public void syncProjects(Set<String> targetProjects, Set<String> currentProjects, Set<String> alreadyRemovedProjects) throws InterruptedException {
        Collection<String> unExistentProjects = CollectionUtils.subtract(targetProjects, currentProjects);
        Collection<String> uselessProjects = CollectionUtils.subtract(currentProjects, targetProjects);

        removeProjects(uselessProjects);
        tryRestoreBeforeCreateProjects(unExistentProjects, alreadyRemovedProjects);

        System.out.println(String.format("%sWaiting %s ms for projects status set.", PROJECT_INDENT, SYNC_INTERVAL));
        Thread.sleep(SYNC_INTERVAL);
        System.out.println(String.format("%sDone.", PROJECT_INDENT));
    }

    private void tryRestoreBeforeCreateProjects(Collection<String> nonExistentProjects, Set<String> removedProjects) {
        for (String nonExistentProject : nonExistentProjects) {

            if (isReserved(nonExistentProject)) {
                continue;
            }

            if (removedProjects.contains(nonExistentProject)) {
                desDogma.unremoveProject(nonExistentProject);
                System.out.println(String.format("%sProject[%s] is restored from removed projects.", PROJECT_INDENT, nonExistentProject));
            } else {
                desDogma.createProject(nonExistentProject);
                System.out.println(String.format("%sProject[%s] is newly created.", PROJECT_INDENT, nonExistentProject));
            }
        }
    }

    private void removeProjects(Collection<String> uselessProjects) {
        for (String uselessProject : uselessProjects) {

            if (isReserved(uselessProject)) {
                continue;
            }

            desDogma.removeProject(uselessProject);
            System.out.println(String.format("%sProject[%s] is removed.", PROJECT_INDENT, uselessProject));
        }
    }
}
