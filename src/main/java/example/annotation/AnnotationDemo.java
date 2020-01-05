package example.annotation;

/**
 * 注释：给人看的；
 *
 * 注解：给程序看的。按照约定好的行为添加注释，程序看到之后要用parser去parse其行为。
 */
@Pikachu(speak = "pikapika on class")
public class AnnotationDemo {

    /**
     * This is for {@link Pikachu#value()}
     */
    @Pikachu("pikachu on field 'hello'")
    private String hello = "hello";

    /**
     * {@link Pikachu#speak()}
     */
    @Pikachu(speak = "pikapika on field 'world'", value = "pikachu on field 'world'")
    private String world = "wo.rld";

    /**
     * {@link Pikachu#value()}
     */
    @Pikachu()
    public void say() {
        System.out.println(hello + world);
    }
}
