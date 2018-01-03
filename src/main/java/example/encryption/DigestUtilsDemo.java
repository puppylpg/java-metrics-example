package example.encryption;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * {@link DigestUtils}: Operations to simplify common {@link java.security.MessageDigest} tasks.
 *
 * @author liuhaibo on 2018/01/03
 */
public class DigestUtilsDemo {

    private static String str = "Hello,world!";
    private byte[] bytes = str.getBytes(StandardCharsets.UTF_8);

    public static void main(String[] args) {
        DigestUtilsDemo demo = new DigestUtilsDemo();
        demo.sha1Hex();
        demo.md5xxx();
    }

    /**
     * pichu@Archer ~ $ echo -n Hello,world! | sha1sum
     * fbb7ff0741faad264c45daa6a5d623c0581def9b  -
     */
    private void sha1Hex() {
        String digestBytes = DigestUtils.sha1Hex(bytes);
        String digestStrs = DigestUtils.sha1Hex(str);
        System.out.println("Digest bytes          = " + digestBytes);
        System.out.println("Digest strs           = " + digestStrs);
    }

    /**
     * pichu@Archer ~ $ echo -n Hello,world! | md5sum
     * 6b7404b3f87e49376fac3b6a43dbe3be  -
     */
    private void md5xxx() {
        System.out.println("Digest bytes          = " + DigestUtils.md5Hex(bytes));
        System.out.println("Digest strs           = " + DigestUtils.md5Hex(str));
    }

}