package example.java8.stream.distinct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liuhaibo on 2017/11/17
 */
public class DistinctByWrapper {

    private static class VideoInfoWrapper {

        private final VideoInfo videoInfo;

        public VideoInfoWrapper(VideoInfo videoInfo) {
            this.videoInfo = videoInfo;
        }

        public VideoInfo unwrap() {
            return videoInfo;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof VideoInfo)) {
                return false;
            }
            VideoInfo vi = (VideoInfo) obj;
            return videoInfo.id.equals(vi.id)
                    && videoInfo.width == vi.width
                    && videoInfo.height == vi.height;
        }

        @Override
        public int hashCode() {
            int n = 31;
            n = n * 31 + videoInfo.id.hashCode();
            n = n * 31 + videoInfo.height;
            n = n * 31 + videoInfo.width;
            return n;
        }
    }

    public static void main(String [] args) {
        List<VideoInfo> list = Arrays.asList(new VideoInfo("123", 1, 2),
                new VideoInfo("456", 4, 5), new VideoInfo("123", 1, 2));

        // VideoInfo --map()--> VideoInfoWrapper ----> distinct(): VideoInfoWrapper --map()--> VideoInfo
        Map<String, VideoInfo> id2VideoInfo = list.stream()
                .map(VideoInfoWrapper::new).distinct().map(VideoInfoWrapper::unwrap)
                .collect(
                Collectors.toMap(VideoInfo::getId, x -> x,
                        (oldValue, newValue) -> newValue)
        );

        id2VideoInfo.forEach((x, y) -> System.out.println("<" + x + ", " + y + ">"));
    }

}

/**
 * Assume that VideoInfo is a class that we can't modify
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
class VideoInfo {
    @Getter
    String id;
    int width;
    int height;
}