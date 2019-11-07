package example.centraldogma.migrage;

import com.fasterxml.jackson.databind.JsonNode;
import com.linecorp.centraldogma.client.CentralDogma;
import com.linecorp.centraldogma.common.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * 文件的删除不像Project和Repo采用标记删除，所以：
 * 在des中删掉src不存在的文件。剩余文件内容直接覆盖更新。删除与更新均使用push完成。
 * <p>
 * Note：如果更新的内容和原始内容一致，dogma会拒绝更新并抛异常ExecutionException(RedundantChangeException)。
 *
 * @author liuhaibo on 2019/11/07
 */
public class FileSync extends Sync {

    FileSync(CentralDogma readDogma, CentralDogma writeDogma) {
        super(readDogma, writeDogma);
    }

    @SuppressWarnings("unchecked")
    public void syncFiles(Map<String, EntryType> targetFilesMap, Map<String, EntryType> currentFilesMap, String repo, String project) throws ExecutionException, InterruptedException {

        if (isReserved(project) || isReserved(repo)) {
            return;
        }

        System.out.println(String.format("%sIn Project[%s]-Repo[%s]", REPO_INDENT, project, repo));

        Set<String> targetFiles = targetFilesMap.keySet();
        Set<String> currentFiles = currentFilesMap.keySet();

        Collection<String> uselessFiles = CollectionUtils.subtract(currentFiles, targetFiles);

        removeFiles(uselessFiles, repo, project);
        createFiles(targetFiles, repo, project, targetFilesMap);
    }

    private void createFiles(Collection<String> nonExistentFiles, String repo, String project, Map<String, EntryType> targetFilesMap) throws ExecutionException, InterruptedException {
        for (String nonExistentFile : nonExistentFiles) {
            try {
                EntryType fileType = targetFilesMap.get(nonExistentFile);
                switch (fileType) {
                    case DIRECTORY:
                        System.out.println(String.format("%sFile[%s] is a directory. omit.", FILE_INDENT, nonExistentFile));
                        break;
                    case TEXT:
                        Entry<String> textFile = srcDogma.getFile(project, repo, Revision.HEAD, Query.ofText(nonExistentFile)).get();
                        push(project, repo, Change.ofTextUpsert(nonExistentFile, textFile.content()));
                        System.out.println(String.format("%sFile[%s] content: %s", FILE_INDENT, nonExistentFile, textFile.content()));
                        break;
                    case JSON:
                        Entry<JsonNode> jsonFile = srcDogma.getFile(project, repo, Revision.HEAD, Query.ofJson(nonExistentFile)).get();
                        push(project, repo, Change.ofJsonUpsert(nonExistentFile, jsonFile.content()));
                        System.out.println(String.format("%sFile[%s] content: %s", FILE_INDENT, nonExistentFile, jsonFile.content()));
                        break;
                }
            } catch (ExecutionException e) {
                if (e.getCause() instanceof RedundantChangeException) {
                    System.out.println(String.format("%sFile[%s] contents in src and dest are identical. No need to modify.", FILE_INDENT, nonExistentFile));
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    private void removeFiles(Collection<String> uselessFiles, String repo, String project) throws ExecutionException, InterruptedException {
        for (String uselessFile : uselessFiles) {
            push(project, repo, Change.ofRemoval(uselessFile));
        }
    }

    private void push(String project, String repo, Change<?> change) throws ExecutionException, InterruptedException {
        PushResult pushResult = desDogma.push(project, repo, Revision.HEAD, "", change).get();
        System.out.println(String.format("%sFile[%s] in Project[%s]-Repo[%s] is %s. PushResult: %s", FILE_INDENT, change.path(), project, repo, change.type(), pushResult.toString()));
    }
}
