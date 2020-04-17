package example.jcf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author liuhaibo on 2020/04/20
 */
public class TraverseDelete {

    public static void main(String... args) {
        List<String> list = new ArrayList<>(Arrays.asList("zero", "first", "second", "third"));

        cStyleLoop(new ArrayList<>(list));
        forLoop(new ArrayList<>(list));
        iteratorLoop(new ArrayList<>(list));
    }

    /**
     * index [0], value=zero, size=4
     * index [1], value=second, size=3
     * index [2], value=third, size=3
     * zero
     * second
     * third
     */
    private static void cStyleLoop(List<String> list) {
        System.out.println("C-STYLE-LOOP:");

        for (int i = 0; i < list.size(); i++) {
            if (i == 1) {
                list.remove(1);
            }
            System.out.println("index [" + i + "], value=" + list.get(i) + ", size=" + list.size());
        }
        list.forEach(System.out::println);
    }

    private static void forLoop(List<String> list) {
        System.out.println("FOR-LOOP:");

        for (String value : list) {
            // loop throws ConcurrentModificationException after remove
            if (value.equals("first")) {
//                list.remove(value);
            }
            System.out.println("value=" + value + ", size=" + list.size());
        }
    }

    private static void iteratorLoop(List<String> list) {
        System.out.println("ITERATOR-LOOP:");

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String value = iterator.next();
            if (value.equals("first")) {
                iterator.remove();
            }
            System.out.println("value=" + value + ", size=" + list.size());
        }
    }
}
