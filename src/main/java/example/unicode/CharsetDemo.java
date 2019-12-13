package example.unicode;

import java.nio.charset.StandardCharsets;

/**
 * @author liuhaibo on 2019/12/13
 */
public class CharsetDemo {

    public static void main(String... args) {
        // abc和皮卡丘都在BMP中，都是一个UTF-16 unit，即2 byte
        // 这和UTF-8（a占一个字节，皮占三个字节）很不相同
        String bmp = "a皮卡b";

        // BOM
        //byte at 0 (FE) is letter? false
        //byte at 1 (FF) is letter? false

        // a
        //byte at 2 (00) is letter? false
        //byte at 3 (61) is letter? true

        // 皮
        //byte at 4 (76) is letter? true
        //byte at 5 (AE) is letter? false

        // 卡
        //byte at 6 (53) is letter? true
        //byte at 7 (61) is letter? true

        // b
        //byte at 8 (00) is letter? false
        //byte at 9 (62) is letter? true
        printBytesUTF16(bmp);

        // a
        //byte at 0 (61) is letter? true

        // 皮
        //byte at 1 (E7) is letter? false
        //byte at 2 (9A) is letter? false
        //byte at 3 (AE) is letter? false

        // 卡
        //byte at 4 (E5) is letter? false
        //byte at 5 (8D) is letter? false
        //byte at 6 (A1) is letter? false

        // b
        //byte at 7 (62) is letter? true
        printBytesUTF8(bmp);

        //char at 0 (a) is letter? true
        //char at 1 (皮) is letter? true
        //char at 2 (卡) is letter? true
        //char at 3 (b) is letter? true
        printChars(bmp);

        System.out.println("\u1F600");

        // 露齿笑：http://www.ltg.ed.ac.uk/~richard/utf-8.cgi?input=1F600&mode=hex
        String emoji = "\u1F600";

        // BOM
        //byte at 0 (FE) is letter? false
        //byte at 1 (FF) is letter? false

        // emoji-smile: 4 bytes
        //byte at 2 (1F) is letter? false
        //byte at 3 (60) is letter? false
        //byte at 4 (00) is letter? false
        //byte at 5 (30) is letter? false
        printBytesUTF16(emoji);

        // emoji-smile: 4 bytes
        //byte at 0 (E1) is letter? false
        //byte at 1 (BD) is letter? false
        //byte at 2 (A0) is letter? false
        //byte at 3 (30) is letter? false
        printBytesUTF8(emoji);

        // high surrogate
        //char at 0 (ὠ) is letter? true
        // low surrogate
        //char at 1 (0) is letter? false
        printChars(emoji);

        // NFC
        String e = "é";

        // BOM
        //byte at 0 (FE) is letter? false
        //byte at 1 (FF) is letter? false
        // NFC: BMP character, 2 byte
        //byte at 2 (00) is letter? false
        //byte at 3 (E9) is letter? false
        printBytesUTF16(e);
        //byte at 0 (C3) is letter? false
        //byte at 1 (A9) is letter? false
        printBytesUTF8(e);
        //char at 0 (é) is letter? true
        printChars(e);
    }

    public static void printBytesUTF16(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_16);

        System.out.printf("%s in UTF-16 mode: %n", s);

        for (int i = 0; i < bytes.length; i++) {
            System.out.println(String.format("byte at %d (%02X) is letter? %b", i, bytes[i], Character.isLetter(bytes[i])));
        }
    }

    public static void printBytesUTF8(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);

        System.out.printf("%s in UTF-8 mode: %n", s);

        for (int i = 0; i < bytes.length; i++) {
            System.out.println(String.format("byte at %d (%02X) is letter? %b", i, bytes[i], Character.isLetter(bytes[i])));
        }
    }

    public static void printChars(String s) {
        System.out.println(String.format("%s.length = %d", s, s.length()));
        char[] chars = s.toCharArray();

        System.out.printf("%s in Char mode: %n", s);

        for (int i = 0; i < chars.length; i++) {
            System.out.println(String.format("char at %d (%c) is letter? %b", i, chars[i], Character.isLetter(chars[i])));
        }
    }
}
