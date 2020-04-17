package example.jcf;

import java.util.*;

/**
 * @author liuhaibo on 2020/04/17
 */
public class LinkedHashMapDemo {

    public static void main(String... args) {
        Map<Integer, String> map = new LinkedHashMap<>(4);
        map.put(1, "111");
        map.put(3, "333");
        map.put(2, "222");
        map.put(21, "222");
        map.put(23, "222");
        map.put(25, "222");
        map.put(22, "222");
        map.put(24, "222");
        map.put(26, "222");
        Set<Integer> keySet = map.keySet();
        // traverse by insertion order
        keySet.forEach(System.out::println);

        // UnsupportedOperationException
//        keySet.add(555);
    }
}
