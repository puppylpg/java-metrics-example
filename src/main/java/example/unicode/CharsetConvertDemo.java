package example.unicode;

import java.nio.charset.StandardCharsets;

public class CharsetConvertDemo {

    public static void main(String... args) {
        String s = "aa皮卡bb";
        // 慡껥趡扢
        System.out.println(new String(s.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_16));
    }
}
