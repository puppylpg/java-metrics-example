package example.file;

import java.io.File;
import java.io.IOException;

/**
 * @author liuhaibo on 2019/12/27
 */
public class FileDemo {

    public static void main(String... args) throws IOException {
        File non_exist = new File("hello.txt");
        fileInfo(non_exist);
        File file = new File("src/main/java/example/file/FileDemo.java");
        fileInfo(file);
        File absolute = new File("/src/main/java/example/file/FileDemo.java");
        fileInfo(absolute);
    }

    private static void fileInfo(File file) throws IOException {
        System.out.println("    => Info for file:" + file);
        System.out.println("name: " + file.getName());
        System.out.println("parent: " + file.getParent());
        System.out.println("path: " + file.getPath());
        System.out.println("absolute path: " + file.getAbsolutePath());
        System.out.println("absolute file: " + file.getAbsoluteFile());
        System.out.println("canonical path: " + file.getCanonicalPath());
        System.out.println("canonical file: " + file.getCanonicalFile());
    }
}
