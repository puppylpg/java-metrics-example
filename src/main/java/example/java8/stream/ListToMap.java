package example.java8.stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liuhaibo on 2017/11/16
 */
public class ListToMap {

    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class VideoInfo {
        @Getter
        String id;
        int width;
        int height;

        public static void main(String [] args) {
            System.out.println(new VideoInfo("123", 1, 2).equals(new VideoInfo("123", 1, 2)));
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof VideoInfo)) {
                return false;
            }
            VideoInfo vi = (VideoInfo) obj;
            return this.id.equals(vi.id)
                    && this.width == vi.width
                    && this.height == vi.height;
        }

        /**
         * If equals() is override, hashCode() must be override, too.
         * 1. if a equals b, they must have the same hashCode;
         * 2. if a doesn't equals b, they may have the same hashCode;
         * 3. hashCode written in this way can be affected by sequence of the fields;
         * 3. 2^5 - 1 = 31. So 31 will be faster when do the multiplication,
         *      because it can be replaced by bit-shifting: 31 * i = (i << 5) - i.
         * @return
         */
        @Override
        public int hashCode() {
            int n = 31;
            n = n * 31 + this.id.hashCode();
            n = n * 31 + this.height;
            n = n * 31 + this.width;
            return n;
        }
    }

    public static void main(String [] args) {
        List<VideoInfo> list = Arrays.asList(new VideoInfo("123", 1, 2),
                new VideoInfo("456", 4, 5), new VideoInfo("123", 1, 2));

        // preferred: handle duplicated data when toMap()
        Map<String, VideoInfo> id2VideoInfo = list.stream().collect(
                Collectors.toMap(VideoInfo::getId, x -> x,
                        (oldValue, newValue) -> newValue)
        );

        System.out.println("No Duplicated1: ");
        id2VideoInfo.forEach((x, y) -> System.out.println("<" + x + ", " + y + ">"));

        // handle duplicated data using distinct(), before toMap()
        // Note that distinct() relies on equals() in the object
        // if you override equals(), hashCode() must be override together
        Map<String, VideoInfo> id2VideoInfo2 = list.stream().distinct().collect(
                Collectors.toMap(VideoInfo::getId, x -> x)
        );

        System.out.println("No Duplicated2: ");
        id2VideoInfo2.forEach((x, y) -> System.out.println("<" + x + ", " + y + ">"));
    }
}
