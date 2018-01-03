package example.bytes;

import java.nio.charset.StandardCharsets;

/**
 * @author liuhaibo on 2018/01/03
 */
public class BytesDemo {
    private static String str = "Hello, world!";
    private static String strChinese = "Hello, world!新年好~";

    public static void main(String [] args) {
        BytesDemo bytesDemo = new BytesDemo();
        bytesDemo.decodeEnglist();
        bytesDemo.decodeChinese();
        bytesDemo.other();
    }

    /**
     * English is always suitable for every charset =.=
     */
    private void decodeEnglist() {
        System.out.println("===> Only English: ");
        byte[] bytes = str.getBytes(StandardCharsets.ISO_8859_1);
        System.out.println("Decode in ISO_8859_1: " + new String(bytes, StandardCharsets.ISO_8859_1));
        System.out.println("Decode in UTF_8: " + new String(bytes, StandardCharsets.UTF_8));
        System.out.println("Decode in default charset: " + new String(bytes));
    }

    /**
     * only corresponding charset is right.
     * ps: if using {@link StandardCharsets#US_ASCII}, {@link #strChinese} never right,
     *      because this charset doesn't contain Chinese characters.
     */
    private void decodeChinese() {
        System.out.println("===> Chinese UTF-16: ");
        byte[] bytes = strChinese.getBytes(StandardCharsets.UTF_16);
        System.out.println("Decode in ISO_8859_1: " + new String(bytes, StandardCharsets.ISO_8859_1));
        System.out.println("Decode in ISO_16: " + new String(bytes, StandardCharsets.UTF_16));
        System.out.println("Decode in UTF_8: " + new String(bytes, StandardCharsets.UTF_8));
        System.out.println("Decode in default charset: " + new String(bytes));

        System.out.println("===> Chinese UTF-8: ");
        bytes = strChinese.getBytes(StandardCharsets.UTF_8);
        System.out.println("Decode in ISO_8859_1: " + new String(bytes, StandardCharsets.ISO_8859_1));
        System.out.println("Decode in ISO_16: " + new String(bytes, StandardCharsets.UTF_16));
        System.out.println("Decode in UTF_8: " + new String(bytes, StandardCharsets.UTF_8));
        System.out.println("Decode in default charset: " + new String(bytes));
    }

    private void other() {
        char[] chars = new char[10];
        str.getChars(2, 5,  chars, 3);
        System.out.println(chars);
    }
}