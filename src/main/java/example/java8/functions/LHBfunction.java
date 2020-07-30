package example.java8.functions;

/**
 * @author liuhaibo on 2020/07/29
 */
public class LHBfunction {

    // Java里，满足某个函数式接口的input和output类型的函数，就是这个函数是接口的一个实现
    static int len(String string) {
        return string.length();
    }

    public static void main(String... args) {

        // 赋值一个函数
        LHB<Integer> lenFunc = LHBfunction::len;

        // 使用函数
        System.out.println(lenFunc.generate("wtf"));
    }
}
