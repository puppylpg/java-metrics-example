package example.singleton.instances;

/**
 * @author liuhaibo on 2017/12/26
 */
public class Simplest {

    private Simplest() {}

    private static final Simplest INSTANCE = new Simplest();

    public static Simplest getInstance() {
        return INSTANCE;
    }

    public String doSomething() {
        return "blabla by " + this.getClass().getSimpleName();
    }
}