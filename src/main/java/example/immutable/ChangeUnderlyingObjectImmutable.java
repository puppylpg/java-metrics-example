package example.immutable;

/**
 * @author liuhaibo on 2018/11/04
 */
public class ChangeUnderlyingObjectImmutable {

    private volatile ComplexKeyValue complexKeyValue = new ComplexKeyValue(1, "hello");

    /**
     * 对于一个不可变对象，如果使用一个新的对象整体把旧的替换掉了，如果原有代码没有执行完，会继续在原有对象的内存上执行。
     * 因为内存的偏移量在sleep之前已经和计算好了。
     * 只有当下一次调用该对象的引用的时候，该对象的引用会告诉当前所指的对象的新的内存地址，才会得到新的值。
     *
     * BEFORE complexKeyValue=example.immutable.ChangeUnderlyingObjectImmutable$ComplexKeyValue@421faab1
     * underlying changed: complexKeyValue=example.immutable.ChangeUnderlyingObjectImmutable$ComplexKeyValue@26ca146f
     * underlying changed: complexKeyValue=example.immutable.ChangeUnderlyingObjectImmutable$ComplexKeyValue@280dfec6
     * underlying changed: complexKeyValue=example.immutable.ChangeUnderlyingObjectImmutable$ComplexKeyValue@2c327f1f
     * underlying changed: complexKeyValue=example.immutable.ChangeUnderlyingObjectImmutable$ComplexKeyValue@6930f4fd
     * AFTER complexKeyValue=example.immutable.ChangeUnderlyingObjectImmutable$ComplexKeyValue@421faab1
     * NOW complexKeyValue=example.immutable.ChangeUnderlyingObjectImmutable$ComplexKeyValue@6930f4fd
     * underlying is changed. i is no longer 1. now complexKeyValue=example.immutable.ChangeUnderlyingObjectImmutable$ComplexKeyValue@6930f4fd
     * sleep is interrupted. quit
     */
    public static void main(String... args) throws InterruptedException {
        ChangeUnderlyingObjectImmutable object = new ChangeUnderlyingObjectImmutable();
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("sleep is interrupted. quit");
                    return;
                }

                // replace
                ComplexKeyValue complexKeyValue = new ComplexKeyValue(2, "world");
                object.complexKeyValue = complexKeyValue;
                System.out.println("underlying changed: complexKeyValue=" + complexKeyValue.toString());

                if (Thread.interrupted()) {
                    System.out.println("interrupted. quit");
                    return;
                }
            }
        });

        t.start();

        if ("world".equals(object.complexKeyValue.get())) {
            System.out.println("bingo");
        }

        System.out.println("NOW complexKeyValue=" + object.complexKeyValue.toString());

        if ("world2".equals(object.complexKeyValue.get())) {
            System.out.println("bingo");
        }

        t.interrupt();
    }

    /**
     * Immutable：只能通过构造函数去赋值，如果想改变对象，只能重新new一个去替换。
     *
     * 在多线程之间读写时，无需加锁。记得用volatile修饰一下，使得修改立即可见就行了。
     */
    private static class ComplexKeyValue {
        private final int i;
        private final String s;

        ComplexKeyValue(int i, String s) {
            this.i = i;
            this.s = s;
        }

        /**
         * 当醒来的时候，依旧是从原来的对象的内存里往下执行的。而不是在新对象上执行
         */
        public String get() throws InterruptedException {
            if (i == 1) {
                System.out.println("BEFORE complexKeyValue=" + this.toString());
                Thread.sleep(5000);
                System.out.println("AFTER complexKeyValue=" + this.toString());
                return s;
            } else {
                System.out.println("underlying is changed. i is no longer 1. now complexKeyValue=" + this.toString());
                return null;
            }
        }
    }
}
