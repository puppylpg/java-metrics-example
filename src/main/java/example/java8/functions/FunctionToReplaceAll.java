package example.java8.functions;

import java.util.function.*;

/**
 * @author liuhaibo on 2017/11/19
 */
public class FunctionToReplaceAll {

    private static void replacePredicate() {
        Function<String, Boolean> func = x -> x.startsWith("a");
        Predicate<String> pre = x -> x.startsWith("a");

        System.out.println(func.apply("ape"));
        System.out.println(pre.test("ape"));
    }

    private static void replaceUnaryOperator() {
        Function<Integer, Integer> func = x -> x * 2;
        UnaryOperator<Integer> uo = x -> x * 2;

        System.out.println(func.apply(6));
        System.out.println(uo.apply(6));
    }

    private static void replaceConsumer() {
        // Consumer<String> con = x -> System.out.println(x);
        Consumer<String> con = System.out::println;
        // return null to satisfy Void
        Function<String, Void> func = x -> {System.out.println(x); return null;};

        con.accept("Hello, World");
        func.apply("Hello, World");
    }


    private static void replaceSupplier() {
        Supplier<String> sup = () -> "Hello";
        // pass Void to satisfy Void
        Function<Void, String> func = Void -> "Hello";

        System.out.println(sup.get());
        // pass null to satisfy Void
        System.out.println(func.apply(null));
    }

    public static void main(String [] args) {
        replacePredicate();
        replaceUnaryOperator();
        replaceConsumer();
        replaceSupplier();
    }
}