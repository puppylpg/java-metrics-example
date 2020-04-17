package example.unicode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 使用ByteBuffer、CharBuffer和Charset字符转字节，并按照规定字节数截断。
 *
 * @author liuhaibo on 2019/11/29
 */
public class StringLength {

    /**
     * 如果是'皮卡丘yoo'，10个字节翻译为'皮卡丘y'，因为'y'正好是第十个字节。
     * 如果是'皮卡皮卡皮卡丘'，10个字节翻译为'皮卡皮[]'，第十个字节为全0，是一个控制字符，翻译不出来。用trim即可！原来trim搞掉的是头尾小于'\u0020'的控制字符啊。
     *
     * 可以在这里查看所有utf-8字符的编号和字节表示 http://www.fileformat.info/info/charset/UTF-8/list.htm
     * 或者在这里直接查 http://www.ltg.ed.ac.uk/~richard/utf-8.cgi?input=%E7%9A%AE&mode=char
     * 比如'皮'用字节表示是11100111 10011010 10101110，十六进制表示为E7 9A AE。
     * 它是第0x76ae个unicode字符，也就是第30382个unicode字符。
     * 就算用utf-16表示，它也是第30382个unicode字符，只不过字节表示（十六进制）变成了76 AE。空间大就是任性，还没超出16个bit所能表示数字的范畴
     * utf-16可以来这里查询 https://www.branah.com/unicode-converter
     * 来这里可以选看utf-8的所有面板 https://www.utf8-chartable.de/unicode-utf8-table.pl
     */
    public static void main(String[] args) throws IOException {
//        String str = "皮卡丘yoo";
        // str.length=6 char(UTF-16)的个数
        // byte.length = 12

        String str = "皮卡皮卡皮卡丘";

        System.out.println("string.length = " + str.length());
        System.out.println("bytes.length = " + str.getBytes(StandardCharsets.UTF_8).length);

        byte[] result = truncateBytes(str, 10);
        for (byte b : result) {
            System.out.println(Integer.toBinaryString((b & 0xFF) + 0x100).substring(1));
        }
        // save to file
//        FileUtils.writeByteArrayToFile(new File("UTF-8_bytes"), result);
        System.out.println("Final translation: " + new String(result, StandardCharsets.UTF_8).trim());
    }

    private static byte[] truncateBytes(String input, int capacity) {

        // with max size
        ByteBuffer out = ByteBuffer.allocate(capacity);
        CharBuffer in = CharBuffer.wrap(input.toCharArray());

        // encode char to byte
        Charset utf8 = StandardCharsets.UTF_8;
        utf8.newEncoder().encode(in, out, true);

        System.out.printf("Encoded %d chars of %d chars in total. Result bytes length=%d.\n", in.position(), input.length(), out.position());
        return out.array();
    }
}
