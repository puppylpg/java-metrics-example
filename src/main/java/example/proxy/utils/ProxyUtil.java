package example.proxy.utils;

import example.proxy.JavaCoder;
import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author liuhaibo on 2018/04/19
 */
public class ProxyUtil {

    public static void main(String[] args) throws IOException {
        byte[] classFile = ProxyGenerator.generateProxyClass("TestProxyGen", JavaCoder.class.getInterfaces());
        File file = new File("/tmp/TestProxyGen.class");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(classFile);
        fos.flush();
        fos.close();
    }
}
