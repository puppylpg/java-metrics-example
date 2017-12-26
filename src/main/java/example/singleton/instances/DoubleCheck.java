package example.singleton.instances;

/**
 * @author liuhaibo on 2017/12/26
 */
public class DoubleCheck {

    private DoubleCheck() {}

    /**
     * volatile is a must!!!
     * 创建一个变量需要哪些步骤呢？
     *      一个是申请一块内存，调用构造方法进行初始化操作；
     *      另一个是分配一个指针指向这块内存。
     *
     * 这两个操作谁在前谁在后呢？JVM规范并没有规定。
     * 那么就存在这么一种情况，JVM是先开辟出一块内存，然后把指针指向这块内存，最后调用构造方法进行初始化。
     *
     * 这是可能出现Thread-A已经让指针指向这块内存，但是还没来得及实例化，CPU时间片耗尽，轮到Thread-B。
     * 这时Thread-B开始进行null-check，发现不为空，就直接用了：NPE
     */
    private volatile static DoubleCheck INSTANCE = null;

    public static DoubleCheck getInstance() {

        // this null-check is to avoid useless lock taking
        if (INSTANCE == null) {

            // null-check and initiation should be atomic operation
            synchronized (DoubleCheck.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DoubleCheck();
                }
            }

        }
        return INSTANCE;
    }

    public String doSomething() {
        return "blabla by " + this.getClass().getSimpleName();
    }
}