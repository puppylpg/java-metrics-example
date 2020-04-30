package example.java8.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author liuhaibo on 2020/04/23
 */
public class SequentialPredicateDemo {

    public static void main(String... args) {
        Predicate<Integer> positive = x -> x > 0;
        Predicate<Integer> negative = x -> x < 0;

        // This is convenient
        Predicate<Integer> nonZero = positive.or(negative);
        // This is what Predicate#or does
        Predicate<Integer> nonZero2 = x -> positive.test(x) || negative.test(x);

        new ArrayList<>(Arrays.asList(-1, 0, 1, 2, 3, 4, 5)).stream()
                // invalid……
//                .filter(positive || negative)
                .filter(nonZero)
                .collect(Collectors.toList()).forEach(System.out::println);
    }
}
