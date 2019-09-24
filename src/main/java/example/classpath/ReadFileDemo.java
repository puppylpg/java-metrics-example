package example.classpath;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * http://note.youdao.com/noteshare?id=0e1c42c5a5e6e00b4a4aa643744df0c8&sub=9D6E791B11054365BBEA4D869532E191
 *
 * @author liuhaibo on 2018/01/11
 */
public class ReadFileDemo {

    public static void main(String[] args) {

        // 从classpath下用ClassLoader读文件，永远都不会有问题
        System.out.println("This is always right:");
        URL url = ReadFileDemo.class.getClassLoader().getResource("config/example.properties");
        try {
            read(url.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 1. 相对于该class读文件，但是普通文件不编译，所以只能读相对该class的java .class文件
        // 2. 相对于classpath读文件，依然是可以的，不会有问题
        System.out.println("This is to read file placed together with this class:");
        URL togetherWithClass = ReadFileDemo.class.getResource("/config/example.properties");
        try {
            read(togetherWithClass.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 相对于当前目录读文件，不能默认当前目录是project root。一旦当前目录变了，就凉凉了……读个锤子……
        System.out.println("This may fail when `pwd` != project root:");
        try {
            read("src/main/resources/config/example.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        System.out.println(content);
    }
}