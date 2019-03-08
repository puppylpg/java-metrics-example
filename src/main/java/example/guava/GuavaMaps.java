package example.guava;

import com.google.common.collect.*;

import java.util.Map;

/**
 * {@link BiMap} - 一一对应
 * {@link Table} - 多key对一个value；每一个key相当于对应一个map
 * {@link Multimap} - 一key对多个value
 * {@link ClassToInstanceMap} - class对instance
 *
 * @author liuhaibo on 2019/01/17
 */
public class GuavaMaps {

    public static void main(String... args) {
        // what's this...
        Map<String, String> aNewMap = Maps.newHashMap();

        // immutable map: use static method(internally, it news a builder, just like below)
        Map<String, Integer> salary = ImmutableMap.<String, Integer>builder()
                .put("John", 1000)
                .put("Jane", 1500)
                .put("Adam", 2000)
                .put("Tom", 2000)
                .build();

        // immutable map: new a builder
        Map<String, Integer> salary2 = new ImmutableMap.Builder<String, Integer>()
                .put("John", 1000)
                .put("Jane", 1500)
                .put("Adam", 2000)
                .put("Tom", 2000)
                .build();

        // immutable sorted map
        ImmutableSortedMap<String, Integer> sortedSalary = new ImmutableSortedMap.Builder<String, Integer>(Ordering.natural())
                .put("John", 1000)
                .put("Jane", 1500)
                .put("Adam", 2000)
                .put("Tom", 2000)
                .build();

        // this way is deprecated
//        ImmutableSortedMap.builder().build();

        // bimap: bidirectional map
        BiMap<String, Integer> words = HashBiMap.create();
        words.put("First", 1);
        words.put("Second", 2);
        words.put("Third", 3);
        // java.lang.IllegalArgumentException: value already present: 1
//        words.put("haha", 1);
        words.forcePut("haha", 1);
        System.out.println(words);

        // multimap
        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("fruit", "apple");
        multimap.put("fruit", "banana");
        multimap.put("pet", "cat");
        multimap.put("pet", "dog");
        System.out.println(multimap);

        // table
        Table<String,String,Integer> distance = HashBasedTable.create();
        distance.put("London", "Paris", 340);
        distance.put("New York", "Los Angeles", 3940);
        distance.put("London", "New York", 5576);
        distance.put("London", "Beijing", 6666);
        distance.put("London", "Zhoukou", 5888);
        System.out.println(distance);

        // class -> instance
        ClassToInstanceMap<Number> numbers = MutableClassToInstanceMap.create();
        numbers.putInstance(Integer.class, 1);
        numbers.putInstance(Double.class, 1.5);
        System.out.println(numbers);
    }
}
