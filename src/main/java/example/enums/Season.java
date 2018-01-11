package example.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuhaibo on 2018/01/11
 */
public enum Season {

    SPRING(-1, "spring"),

    SUMMER(0, "summer"),

    AUTUMN(1, "autumn"),

    WINTER(2, "winter"),

    UNKNOWN(3, "unknown");

    private int index;
    private String text;

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    Season(int index, String text) {
        this.index = index;
        this.text = text;
    }

    private static final Map<Integer, Season> indexToSeasons = new HashMap<>();

    static {
        for (Season season : Season.values()) {
            indexToSeasons.put(season.getIndex(), season);
        }
    }

    /**
     * get season by index
     *
     * @param index index
     * @return a Season
     */
    public static Season parseByIndex(int index) {
        return indexToSeasons.getOrDefault(index, UNKNOWN);
    }

    /**
     * get the season by a string, string should be UPPER case
     * <p>
     * (because enum value is UPPER case)
     *
     * @param text
     * @return
     */
    public static Season parseByText(String text) {
        return Season.valueOf(text);
    }

    public static void main(String[] args) {
        System.out.println("Print all the seasons:");
        indexToSeasons.forEach((x, y) -> System.out.println(x + y.getText()));

        System.out.println("Print season 2:");
        System.out.println(parseByIndex(2));
        System.out.println(parseByText("WINTER"));
    }
}