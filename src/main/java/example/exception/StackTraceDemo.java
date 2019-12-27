package example.exception;

/**
 * @author liuhaibo on 2019/12/25
 */
public class StackTraceDemo {

    public static void main(String... args) {
        inner("pikachu");

        System.out.println("in main ->");
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        System.out.println("trace size: " + elements.length);
        for (StackTraceElement element : elements) {
            System.out.println(String.join(
                    "::",
                    element.getClassName(),
                    element.getMethodName(),
                    String.valueOf(element.getLineNumber())
            ));
        }
    }

    private static void inner(String s) {
        System.out.println("Inner: " + s);
        innerMost(s);
    }

    private static void innerMost(String s) {
        System.out.println("InnerMost: " + s);
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        System.out.println("trace size: " + elements.length);
        for (StackTraceElement element : elements) {
            System.out.println(element);
        }

        System.out.println("parent info ->");
        StackTraceElement parent = elements[2];
        System.out.printf("Class: %s; Method: %s; fileName: %s; lineNumber: %d%n", parent.getClassName(), parent.getMethodName(), parent.getFileName(), parent.getLineNumber());
    }
}
