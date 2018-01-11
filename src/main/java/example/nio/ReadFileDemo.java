package example.nio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author liuhaibo on 2018/01/11
 */
public class ReadFileDemo {

    private static final String FILE = "config/example.properties";

    public static void main(String[] args) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(FILE)), StandardCharsets.UTF_8);
        System.out.println(content);
    }
}