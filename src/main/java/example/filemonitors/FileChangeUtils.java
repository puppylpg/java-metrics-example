package example.filemonitors;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

/**
 * @author liuhaibo on 2018/01/08
 */
public class FileChangeUtils {

    public static void monitorDirectory(String path, FileChange fileChange) {
        // 1. observer
        File directory = new File(path);
        FileAlterationObserver observer = new FileAlterationObserver(
                directory,
                pathname -> pathname.getName().toLowerCase().endsWith(".txt"));

        // 2. listener
        observer.addListener(new ConfigFileChangeListener(fileChange));

        // 3. monitor: start another thread to monitor files, using Thread.sleep(interval)
        long interval = 5 * 1000;
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval);
        monitor.addObserver(observer);
        try {
            monitor.start();
        } catch (Exception e) {
            System.out.println("start monitor error, path=" + path);
        }
    }
}