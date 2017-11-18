package example.java8.functions;

import java.util.*;
import java.util.function.*;

/**
 * @author liuhaibo on 2017/11/19
 */
public class AllFunctions {

    ///// Function: T --> ||Function|| --> R

    private static void display(Function<Integer, String> func) {
        System.out.println(func.apply(10));
    }

    private static void useOfFunction() {
        System.out.println("Function: T --> ||Function|| --> R");

        Function<Integer, String> func = x -> "Make it twice: " + x * 2;

        // use lambda directly
        String result = func.apply(10);
        System.out.println(result);

        // use a function to operate lambda
        display(func);
    }

    ///// Supplier:: null --> ||Supplier|| --> T

    private static void display(Supplier<Integer> arg) {
        System.out.println(arg.get());
    }

    private static void useOfSupplier() {
        System.out.println("Supplier:: null --> ||Supplier|| --> T");

        Supplier<Double> sup = () -> Math.random() * 10;

        // use lambda directly
        Double result = sup.get();
        System.out.println("Random double in [0, 10): " + result);

        // use a function to operate lambda
        display(() -> 10);
        display(() -> (int) (Math.random() * 100));
    }

    ///// Predicate: T --> ||Predicate|| --> boolean

    private static void display(Predicate<String> pre) {
        System.out.println(pre.test("Hello"));
    }

    private static void useOfPredicate() {
        System.out.println("Predicate: T --> ||Predicate|| --> boolean");

        List<String> list = new ArrayList<>(Arrays.asList("cat", "dog", "bear", "cheetah"));

        // use lambda directly
        list.removeIf(element -> element.startsWith("c"));
        System.out.println(list.toString());

        List<String> list2 = new ArrayList<>(Arrays.asList("cat", "dog", "bear", "cheetah"));
        Predicate<String> pre = x -> x.startsWith("c");

        // use lambda directly
        list2.stream().filter(pre).forEach(System.out::println);

        // use a function to operate lambda
        display(pre);
    }

    ///// Consumer: T --> ||Consumer|| --> null

    private static void display(Consumer<Integer> con) {
        con.accept(8);
    }

    private static void useOfConsumer() {
        System.out.println("Consumer: T --> ||Consumer|| --> null");

        Consumer<Integer> consumer = x -> System.out.println(x + " is consumed.");

        // use lambda directly
        consumer.accept(1);

        // use a function to operate lambda
        display(consumer);
    }

    ///// UnaryOperand: T --> ||UnaryOperand|| --> T

    private static String display(UnaryOperator<String> uo) {
        return uo.apply("Hello");
    }

    private static void useOfUnaryOperand() {
        System.out.println("UnaryOperand: T --> ||UnaryOperand|| --> T");

        List<Integer> list = new ArrayList<>(Arrays.asList(5, 6, 7));

        // use lambda directly
        list.replaceAll(element -> element + 10);
        System.out.println(list);

        // use a function to operate lambda
        UnaryOperator<String> uo = x -> x + x;
        System.out.println(display(uo));
    }

    ///// BiConsumer: T, U --> ||BiConsumer|| --> null

    private static void display(BiConsumer<String, String> bc) {
        bc.accept("Ni", "Hao");
    }

    private static void useOfBiConsumer() {
        System.out.println("BiConsumer: T, U --> ||BiConsumer|| --> null");
        Map<String, String> map = new HashMap<>();
        map.put("Hello", "World");

        BiConsumer<String, String> bc = (x, y) -> System.out.println(y + x);

        // use lambda directly
        map.forEach(bc);

        // use a function to operate lambda
        display(bc);
    }

    ///// Main

    public static void main(String[] args) {
        useOfFunction();
        useOfSupplier();
        useOfPredicate();
        useOfConsumer();
        useOfUnaryOperand();

        useOfBiConsumer();
    }
}