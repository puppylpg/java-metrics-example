package example.singleton.instances;

/**
 * @author liuhaibo on 2017/12/26
 */
public class StaticInnerClass {

    private StaticInnerClass() {}

    private static class Holder {
        private static final StaticInnerClass INSTANCE = new StaticInnerClass();
    }

    public static StaticInnerClass getInstance() {
        return Holder.INSTANCE;
    }

    public String doSomething() {
        return "blabla by " + this.getClass().getSimpleName();
    }
}