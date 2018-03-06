package example.reference;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @author liuhaibo on 2018/03/06
 */
public class WeakRefDemo {

    public static void main(String... args) {

        // all these objects have a strong reference
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();

        // other references to these objects
        Object strongA = a;
        SoftReference<Object> softB = new SoftReference<>(b);
        WeakReference<Object> weakC = new WeakReference<>(c);

        // free the former strong references to these objects:

        // there is still a strong reference(strongA) to the first object
        a = null;
        // only a soft reference(softB) refers to the second object
        b = null;
        // only a weak reference(weakC) refers to the third object
        c = null;

        System.out.println("Before gc...");
        System.out.println(String.format("strongA = %s, softB = %s, weakC = %s", strongA, softB.get(), weakC.get()));

        System.out.println("Run GC...");

        System.gc();

        // object with only soft reference will be cleaned only memory is not enough: 用来做缓存很不错
        // object with only weak reference will be cleaned after a gc operation:
        // ThreadLocalMap的key不是ThreadLocal而是ThreadLocal的弱引用，可以防止线程池里的线程被重用的时候，threadLocals变量里的东西也被共享
        System.out.println("After gc...");
        System.out.println(String.format("strongA = %s, softB = %s, weakC = %s", strongA, softB.get(), weakC.get()));
    }
}
