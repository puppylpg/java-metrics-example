package example.reference;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * @author liuhaibo on 2018/03/07
 */
public class WeakHashMapDemo {

    public static void main(String... args) {
        HashMap<UniqueImageIndex, VeryBigImage> cache = new HashMap<>();
        WeakHashMap<UniqueImageIndex, VeryBigImage> weakCache = new WeakHashMap<>();

        UniqueImageIndex firstIndex = new UniqueImageIndex("first");
        UniqueImageIndex secondIndex = new UniqueImageIndex("second");
        VeryBigImage firstImage = new VeryBigImage(1);
        VeryBigImage secondImage = new VeryBigImage(2);

        // hashMap也算有强引用，所以人称会内存泄漏
        cache.put(firstIndex, firstImage);
        // weakHashMap靠的是是弱引用
        weakCache.put(secondIndex, secondImage);

        // 删除显示的强引用
        firstIndex = null;
        secondIndex = null;
        firstImage = null;
        secondImage = null;

        System.out.println("== Before GC ==");
        System.out.println("HashMap: " + cache);
        System.out.println("WeakHashMap: " + weakCache);


        System.out.println("== GCing... ==");
        System.gc();

        System.out.println("== After GC ==");
        System.out.println("HashMap: " + cache);
        // empty
        System.out.println("WeakHashMap: " + weakCache);
    }

    private static class VeryBigImage {
        int size;

        VeryBigImage(int i) {
            size = i;
        }
    }

    private static class UniqueImageIndex {
        String name;

        UniqueImageIndex(String name) {
            this.name = name;
        }
    }
}
