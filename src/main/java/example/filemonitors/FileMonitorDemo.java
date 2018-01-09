package example.filemonitors;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

/**
 * @author liuhaibo on 2018/01/08
 */
public class FileMonitorDemo {

    private static final String DIR = "config/fileChanges";

    private static void loadFiles() {
        try {
            Files.list(Paths.get(DIR))
                    .filter(filePath -> filePath.getFileName().toString().toLowerCase().endsWith(".txt"))
                    .forEach(filePath -> {
                        try {
                            readFile(filePath);
                        } catch (IOException e) {
                            System.out.println("Error when reading file: " + filePath);
                        }
                    });
        } catch (IOException e) {
            System.out.println(String.format("Path '%s' doesn't exist", DIR));
        }
    }

    private static void readFile(Path path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path.toFile()));

        System.out.println("\n" + Calendar.getInstance().getTime() + " " + path);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    static {
        loadFiles();
//        FileChangeUtils.monitorDirectory(DIR, FileMonitorDemo::loadFiles);
        FileChangeUtils.monitorDirectory(DIR, () -> loadFiles());
    }

    public static void main(String[] args) {
        System.out.println("Hello, world~");
    }
}