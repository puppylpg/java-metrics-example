package example.java8.stream.distinct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 1. use Function to extract keys;
 * 2. use Predicate to assert key;
 * 3. use filter() to filter keys.
 *
 * @author liuhaibo on 2017/11/17
 */
public class DistinctByFilterAndLambda {

    /**
     * Assume that VideoInfo is a class that we can't modify
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class VideoInfo {
        @Getter
        String id;
        int width;
        int height;
    }

    public static void main(String[] args) {
        List<VideoInfo> list = Arrays.asList(new VideoInfo("123", 1, 2),
                new VideoInfo("456", 4, 5), new VideoInfo("123", 1, 2));

        // Get distinct only
        Map<String, VideoInfo> id2VideoInfo = list.stream().filter(distinctByKey(vi -> vi.getId())).collect(
                Collectors.toMap(VideoInfo::getId, x -> x,
                        (oldValue, newValue) -> newValue)
        );

        id2VideoInfo.forEach((x, y) -> System.out.println("<" + x + ", " + y + ">"));
    }

    /**
     * If a key could not be put into ConcurrentHashMap, that means the key is duplicated
     * @param keyExtractor a mapping function to produce keys
     * @param <T> the type of the input elements
     * @return true if key is duplicated; else return false
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}