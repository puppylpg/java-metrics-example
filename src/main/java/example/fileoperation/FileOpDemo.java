package example.fileoperation;

import java.io.File;
import java.io.IOException;

/**
 * @author liuhaibo on 2018/01/09
 */
public class FileOpDemo {

    public static void main(String[] args) {
        FileOpDemo demo = new FileOpDemo();
        final String DIR = "config/tmp";
        System.out.println("user.home: " + System.getProperty("user.home"));
        System.out.println("user.dir: " + System.getProperty("user.dir"));
        demo.testCurrentPath(DIR);
        demo.testParentPath(DIR);
        demo.testRelativePath(DIR);
        demo.testAbsolutePath();
    }

    private void testCurrentPath(String dir) {
        File current = new File(dir + "/./current");
        System.out.println("Current Path: ");
        printPath(current);
    }

    private void testParentPath(String dir) {
        System.out.println("Parent Path: ");
        File parent = new File(dir + "/../parent");
        printPath(parent);
    }

    private void testRelativePath(String dir) {
        System.out.println("Relative Path: ");
        File relative = new File(dir + "/../.././relative");
        printPath(relative);
    }

    private void testAbsolutePath() {
        System.out.println("Absolute Path: ");
        File absolute = new File("/tmp/haha/xixi/lol");
        printPath(absolute);
    }

    private void printPath(File current) {
        // last part of the path
        System.out.println("Name: " + current.getName());
        // what you write when newing file
        System.out.println("Path: " + current.getPath());
        // absolute path: user.dir + getPath()
        System.out.println("AbsolutePath: " + current.getAbsolutePath());
        try {
            // correct resolved path for me
            System.out.println("CanonicalPath: " + current.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}