package example.filemonitors.singlefile;

import org.apache.log4j.helpers.FileWatchdog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * {@link FileWatchdog}默认设置为守护线程，这样主程序在退出之后也就不再监控了。
 * <p>
 * 这里为了演示，设置其为非守护线程。
 *
 * @author liuhaibo on 2018/01/11
 */
public class MonitorSingleFile {

    private static final String FILE = "config/example.properties";

    private static void loadFile() {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(FILE)));
        } catch (IOException e) {
            System.out.println("Error: file " + FILE + " doesn't exist!");
        }
        System.out.println("\n" + new Date());
        System.out.println(content);
    }

    static {
        FileWatchdog watchdog = new FileWatchdog(FILE) {
            @Override
            protected void doOnChange() {
                loadFile();
            }
        };
        watchdog.setName("config-file-watchdog");
        watchdog.setDelay(5 * 1000);
        // deliberately
        watchdog.setDaemon(false);
        watchdog.start();
    }

    public static void main(String[] args) {
        System.out.println("hello, world~");
    }
}