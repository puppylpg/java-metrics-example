package example.guava;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

/**
 * @author liuhaibo on 2019/01/17
 */
public class GuavaSets {

    public static void main(String... args) {
        // what's this...
        Set<String> aNewSet = Sets.newHashSet();

        // modification
        Set<Character> first = ImmutableSet.of('a', 'b', 'c');
        Set<Character> second = ImmutableSet.of('b', 'c', 'd');
        Set<Character> union = Sets.union(first, second);
        Set<Character> intersection = Sets.intersection(first, second);
        // 两个集合的笛卡尔积
        Set<List<Character>> cartesianProduct = Sets.cartesianProduct(ImmutableList.of(first, second));
        System.out.println(cartesianProduct);
        // the power set – the set of all possible subsets
        Set<Set<Character>> powerSet = Sets.powerSet(first);
        powerSet.forEach(System.out::println);
    }
}
