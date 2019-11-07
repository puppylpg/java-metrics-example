package example.exception;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 当对外部资源进行处理（例如读或写）时，如果遭遇了异常，
 * 且在随后的关闭外部资源过程中，又遭遇了异常，那么你catch到的将会是对外部资源进行处理时遭遇的异常，
 * 关闭资源时遭遇的异常将被“抑制”但不是丢弃，通过异常的getSuppressed方法，可以提取出被抑制的异常。
 *
 * @author liuhaibo on 2019/11/07
 */
public class TryWithResourceDemo {

    public static void main(String... args) {
        try {
            autoCloseAndAddSuppressed();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Suppressed exceptions: ");

            // 取出关闭资源时产生的异常
            Throwable[] suppressed = e.getSuppressed();
            for (int i = 0; i < suppressed.length; i++) {
                System.out.println("Suppressed exception " + i + ": ");
                suppressed[i].printStackTrace();
            }
        }
    }

    /**
     * 一个try-with-resource会被拓展为下面缩进中的try-catch-finally，而且会把处理异常连带关闭异常一起抛出来。
     *
     *     private static void autoCloseAndAddSuppressed() throws IOException {
     *         try {
     *             FileInputStream inputStream = new FileInputStream(new File("FileNotExist"));
     *
     *             // 注意这个var1
     *             Throwable var1 = null;
     *
                 *             try {
                 *                 System.out.println(inputStream.read());
                 *             } catch (Throwable var11) {
                 *
                 *                 // 如果出异常，var1会被抛出。var1存的是处理资源时的异常
                 *                 var1 = var11;
                 *                 throw var11;
                 *             } finally {
                 *                 if (inputStream != null) {
                 *                     if (var1 != null) {
                 *                         try {
                 *                             inputStream.close();
                 *                         } catch (Throwable var10) {
                 *
                 *                             // 同时如果关闭资源的时候又出了异常，也会被放到va1里。这是1.7加的
                 *                             var1.addSuppressed(var10);
                 *                         }
                 *                     } else {
                 *                         inputStream.close();
                 *                     }
                 *                 }
                 *
                 *             }
     *
     *         } catch (IOException var13) {
     *             throw new IOException(var13.getMessage(), var13);
     *         }
     *     }
     * @throws IOException
     */
    private static void autoCloseAndAddSuppressed() throws IOException {
        try (FileInputStream inputStream = new FileInputStream(new File("FileNotExist"))) {
            System.out.println(inputStream.read());
        } catch (IOException e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
