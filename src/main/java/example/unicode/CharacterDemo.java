package example.unicode;

import static example.unicode.CharsetDemo.*;

/**
 * https://stackoverflow.com/questions/9834964/char-to-unicode-more-than-uffff-in-java
 * https://stackoverflow.com/a/47505451/7676237
 * http://russellcottrell.com/greek/utilities/SurrogatePairCalculator.htm
 */
public class CharacterDemo {

    public static void main(String... args) {
        convertSupplementaryPlaneCharacter();
        supplementaryPlaneCharacterLength();
        charToInt();
        charMethod();
    }

    private static void convertSupplementaryPlaneCharacter() {
        System.out.println("ways to convert supplementary plane character: ");
        int smileCodePoint = 0x1F600;

        int smileHighSurrogate = 0xD83D;
        int smileLowSurrogate = 0xDE00;

        // use surrogate pair
        String smile0 = "\uD83D\uDE00";
        System.out.printf("simle0: %s%n", smile0);

        // use StringBuilder code point
        String smile1 = new StringBuilder().appendCodePoint(smileCodePoint).toString();
        System.out.printf("simle1: %s%n", smile1);

        printBytesUTF16(smile1);
        printBytesUTF8(smile1);
        printChars(smile1);

        // use Character code point
        String smile2 = new String(Character.toChars(smileCodePoint));
        System.out.printf("simle2: %s%n", smile2);
    }

    private static void supplementaryPlaneCharacterLength() {
        System.out.println("supplementary plane character length: ");
        // 冬，不是冬
        int notDongCodePoint = 0x2F81A;
        int notDongHighSurrogate = 0xD87E;
        int notDongLowSurrogate = 0xDC1A;
        // 一个supplementary plane字符两个char
        char[] notDongChars = Character.toChars(notDongCodePoint);
        String notDongString = new String(notDongChars);

        // 2
        System.out.printf("notDongChars.length = %d%n", notDongChars.length);
        // 2
        System.out.printf("notDongString.length() = %d%n", notDongString.length());
    }

    /**
     * char就是两个字节，转成int就是两个字节所代表的数值。
     *
     * BMP字符就是一个char，所以转成int就是码点值；
     * 补充面板字符是两个char，所以转成int分别是high/low surrogate的码点值。
     */
    private static void charToInt() {
        System.out.println("char to int: ");
        // 皮
        int piCJK = 0x76AE;
        // 76AE
        System.out.printf("char to int: %x%n", (int)Character.toChars(piCJK)[0]);

        // 冬，不是冬
        int notDongCodePoint = 0x2F81A;
        // high surrogate: D87E
        System.out.printf("char to int: %x%n", (int)Character.toChars(notDongCodePoint)[0]);
        // low surrogate: DC1A
        System.out.printf("char to int: %x%n", (int)Character.toChars(notDongCodePoint)[1]);
    }

    /**
     * char为参数的方法只支持BMP字符，因为补充面板字符实际传进去的是high surrogate；
     * int为参数的方法支持所有码点值
     */
    private static void charMethod() {
        System.out.println("char method: ");
        // 冬，不是冬
        int notDongCodePoint = 0x2F81A;
        // 一个supplementary plane字符两个char
        char[] notDongChars = Character.toChars(notDongCodePoint);
        // char method: can only pass high surrogate, which is not a letter
        // false
        System.out.printf("Character.isLetter(char): %b%n", Character.isLetter(notDongChars[0]));
        // int method: can pass a code point
        // true
        System.out.printf("Character.isLetter(int): %b%n", Character.isLetter(notDongCodePoint));
    }
}
