package example.mystream;

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

@AllArgsConstructor
@NoArgsConstructor
@ToString
class VideoInfo {
    @Getter
    String id;
    int width;
    int height;

    public static void main(String [] args) {
        System.out.println(new VideoInfo("123", 1, 2).equals(new VideoInfo("123", 1, 2)));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VideoInfo) {
            VideoInfo vi = (VideoInfo) obj;
            if (this.id.equals(vi.id)
                    && this.width == vi.width
                    && this.height == vi.height) {
                return true;
            }
        }
        return false;
    }

    // if a equals b, they must have the same hashCode
    // if a doesn't equals b, they may have the same hashCode
    @Override
    public int hashCode() {
        return this.id.hashCode() * 37
                + this.height + this.width;
    }
}
