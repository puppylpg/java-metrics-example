package example.immutable;

/**
 * @author liuhaibo on 2018/11/04
 */
public class ModifyUnderlyingObjectMutable {

    private ComplexKeyValue complexKeyValue = new ComplexKeyValue(1, "hello");

    /**
     * underlying changed
     * underlying changed
     * underlying changed
     * underlying changed
     * bingo
     * sleep is interrupted. quit
     */
    public static void main(String... args) throws InterruptedException {
        ModifyUnderlyingObjectMutable object = new ModifyUnderlyingObjectMutable();
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("sleep is interrupted. quit");
                    return;
                }

                // replace
                object.complexKeyValue.set(2, "world");
                System.out.println("underlying changed");

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

        t.interrupt();
    }

    /**
     * Mutable object。在多线程之间读写的时候，需要加锁。
     */
    private static class ComplexKeyValue {
        private int i;
        private String s;

        ComplexKeyValue(int i, String s) {
            this.i = i;
            this.s = s;
        }

        public String get() throws InterruptedException {
            if (i == 1) {
                Thread.sleep(5000);
                return s;
            }
            return null;
        }

        public void set(int i, String s) {
            this.i = i;
            this.s = s;
        }
    }
}
