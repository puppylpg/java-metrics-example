package example.java8.stream;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuhaibo on 2019/12/19
 */
public class JoinString {

    public static void main(String... args) {
        List<String> empty = Collections.emptyList();
        System.out.println(String.join(" <--> ", empty));

        List<String> list = Arrays.asList("Hello", "world", "!");

        System.out.println(list.stream().collect(Collectors.joining(" --> ", "[", "]")));

        System.out.println(String.join(" <--> ", list));
        System.out.println(String.join("\n", list));
    }
}
