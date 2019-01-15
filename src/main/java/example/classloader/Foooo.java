package example.classloader;

/**
 * @author liuhaibo on 2019/01/03
 */
public class Foooo {

    public static String s = "Karrigan";

    public static void changeString(String string) {
        System.out.println("before change: s=" + s);
        s = string;
        System.out.println("after change: s=" + s);
    }


    public void bar() {
        System.out.println("bar");
    }

    public static void staticBar() {
        System.out.println("static bar");
    }
}
