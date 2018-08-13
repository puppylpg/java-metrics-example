package example.io.memoryio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author liuhaibo on 2018/08/13
 */
public class GzipIODemo {

    public static void main(String... args) {
        try {
            String str = "Hello, world!";
            System.out.println("str bytes: " + str.getBytes(StandardCharsets.UTF_8.name()));

            byte[] gzipBytes = compress(str);

            String target = uncompress(gzipBytes);
            System.out.println("final string: " + target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * String -> gziped byte[].
     *
     * @param str
     * @return
     * @throws IOException
     */
    private static byte[] compress(String str) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(str.getBytes(StandardCharsets.UTF_8.name()));
        gzipOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * gziped bytes -> InputStream(gziped bytes) -> OutputStream -> new String.
     *
     * 这里，由于bytes是gzip压缩过的，所以必须转成GZIPInputStream，然后读出来。
     * 读出来可以选择读到OutputStream中，也可以简单的读入一个bytes数组，但是鉴于
     * ByteArrayOutputStream已经内部维护了一个自动增长的byte array，所以就是用它好了，不再自己手动搞。
     *
     *
     * IMPORTANT: 必须记得每次读了多少，然后往out里写的时候，只写那么长。否则写了很多未初始化的字节，
     * 反序列化出来之后会面后带上一大堆不明意义的东西……
     *
     * @param gzipBytes
     * @return
     * @throws IOException
     */
    private static String uncompress(byte[] gzipBytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(gzipBytes);
        GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] tmp = new byte[32];
        int len;
        // len is a must
        while ((len = gzipInputStream.read(tmp)) >= 0) {
            System.out.println("get bytes: " + tmp);
            out.write(tmp, 0, len);
        }
        gzipInputStream.close();
        System.out.println("out bytes: " + out.toByteArray());
        return new String(out.toByteArray());
    }
}
