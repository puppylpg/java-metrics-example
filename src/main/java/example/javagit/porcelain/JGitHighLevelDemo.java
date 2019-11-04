package example.javagit.porcelain;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RevertCommand;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * porcelain APIs - front-end for common user-level actions (similar to Git command-line tool)
 */
public class JGitHighLevelDemo {

    public static void main(String[] args) throws Exception {
        // create a clean repository
        File path = new File("target/git-repo");
        if (path.exists()) {
            FileUtils.deleteDirectory(path);
        }
        Git git = Git.init().setDirectory(path).call();
        System.out.println("Created a new repository at " + git.getRepository().getDirectory());

        // Create a new file and add it to the index
        File file = new File(path, "file1.txt");

        //commit 1
        FileUtils.writeStringToFile(file, "Line 1\n", StandardCharsets.UTF_8, true);
        git.add().addFilepattern("file1.txt").call();
        RevCommit rev1 = git.commit().setAuthor("test", "test@test.com").setMessage("Commit Log 1").call();

        // commit 2
        FileUtils.writeStringToFile(file, "Line 2\n", StandardCharsets.UTF_8, true);
        git.add().addFilepattern("file1.txt").call();
        RevCommit rev2 = git.commit().setAll(true).setAuthor("test", "test@test.com").setMessage("Commit Log 2").call();

        // commit 3
        FileUtils.writeStringToFile(file, "Line 3\n", StandardCharsets.UTF_8, true);
        git.add().addFilepattern("file1.txt").call();
        RevCommit rev3 = git.commit().setAll(true).setAuthor("test", "test@test.com").setMessage("Commit Log 3").call();

        System.out.println("=====");
        // AnyObjectId -> ObjectId -> RevObject(eg: RevBlob/RevCommit/RevTree/RevTag)
        System.out.println("reversion id: " + rev3.getId());
        // 只能得到SHA-1的name，也就是十六进制字符串，
        // 想得到原始bytes没有getBytes()，只有copyRawTo(xxx)，将bytes拷贝到装bytes的容器，
        // 而不是直接输出！因为输出并没有什么意义。但是返回bytes数组貌似也不是不行啊……
        System.out.println("reversion id's name: " + rev3.getId().getName());
        System.out.println("=====");

        RevertCommand revertCommand = git.revert();
        // revert to revision 3
        revertCommand.include(rev3.getId());
        RevCommit revCommit = revertCommand.call();


        // print logs
        Iterable<RevCommit> gitLog = git.log().call();
        for (RevCommit logMessage : gitLog) {
            System.out.println(logMessage.getName() + " - " + logMessage.getFullMessage());
        }
    }
}