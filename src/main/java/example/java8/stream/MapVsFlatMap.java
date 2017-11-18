package example.java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author liuhaibo on 2017/11/15
 */
public class MapVsFlatMap {
    public static void main(String [] args) {
        // a text
        List<String> text = Arrays.asList(
                "Hello, I am so happy!",
                "What about you?",
                "Where are you from?",
                "This is a good example for java 8."
        );

        // use orElse() in case of null in Optional
        int maxWord1 = useMap(text).orElse(-1);
        System.out.println("Most words of a line(-1 means error): " + maxWord1);

        int maxWord2 = useFlatMap(text).orElse(-1);
        System.out.println("Most words of a line(-1 means error): " + maxWord2);

        otherFlatMapUsage(text);

    }

    /**
     * {@link java.util.stream.Stream#map(Function)} is only used to get a one-to-one mapper
     */
    private static Optional<Integer> useMap(List<String> list) {
        return list.stream().map(line -> line.split(" ").length).reduce(Math::max);
    }

    /**
     * {@link java.util.stream.Stream#flatMap(Function)} is used to get a one-to-many mapper
     * if you really want to make one-to-one with flatMap(), then it should be changed into one-to-List(one).
     * Note: flatMap() can also strip outer Stream() if put it simply.
     * ref: https://stackoverflow.com/a/47299117/7676237
     */
    private static Optional<Integer> useFlatMap(List<String> text) {
        // `Arrays.asList(xxx).stream()` could be replaced by `Stream.of(xxx)`
        // text.stream().flatMap(line -> Stream.of(line.split(" ").length)).reduce(Math::max);
        return text.stream().flatMap(line -> Arrays.asList(line.split(" ").length).stream()).reduce(Math::max);
    }

    /**
     * {@link java.util.stream.Stream#flatMap(Function)} is used to get a one-to-many mapper
     * and then flatten the resulting elements into a new stream
     */
    private static void otherFlatMapUsage(List<String> text) {
        // flatMap() can only convert one into many, and then flatten them into one stream
        long allWord1 = text.stream().flatMap(line -> Arrays.asList(line.split(" ")).stream()).count();
        System.out.println("Total number of words for the text: " + allWord1);

        // optimize 1: maptoLong() is indeed map(), but returns a LongStream, and can use sum() etc. for statistic
        long allWord2 = text.stream().mapToLong(line -> line.split(" ").length).sum();
        System.out.println("Total number of words for the text: " + allWord2);

        // optimize 2: change a String[] into stream with Arrays.stream(), rather than String[]->List->stream()
        long allDifferentWord = text.stream().flatMap(line -> Arrays.stream(line.split(" "))).distinct().count();
        System.out.println("Total number of distinct words for the text: " + allDifferentWord);

        System.out.println();
    }

}