package example.concurrency.tl;

/**
 * @author liuhaibo on 2018/05/23
 */
public class ThreadLocalDemo {

    /**
     * 每个Thread都有一个{@link Thread#threadLocals}，它是一个map：{@link java.lang.ThreadLocal.ThreadLocalMap}
     * 这个map的key -> value是{@link java.lang.ThreadLocal.ThreadLocalMap.Entry}：{@link ThreadLocal} -> {@link Object}；
     * 内容是这样的：
     * | TL_INT    -> 6 |
     * | TL_STRING -> "Hello, world"|
     *
     * 所以我们在取值的时候，理论上应该先找到当前thread（这个就免了，jvm知道当前线程是什么），再根据key（这个比较特殊，变成了KEY.get()）去取value。
     * 因此，最终变成了TL_INT.get()就取到值了。所以看起来有些奇怪吧。但实际上需要提供的信息确实只有一个KEY就足够了。
     *
     * Note：但实际上，Entry的key是WeakReference<ThreadLocal<?>>，这么做可以防止线程池里的线程被重用的时候，{@link Thread#threadLocals}变量里的东西也被共享
     */
    private static final ThreadLocal<Integer> TL_INT = ThreadLocal.withInitial(() -> 6);
    private static final ThreadLocal<String> TL_STRING = ThreadLocal.withInitial(() -> "Hello, world");

    public static void main(String... args) {
        System.out.println(TL_INT.get());
        TL_INT.set(TL_INT.get() + 1);
        System.out.println(TL_INT.get());
        TL_INT.remove();
        System.out.println(TL_INT.get());
    }
}
