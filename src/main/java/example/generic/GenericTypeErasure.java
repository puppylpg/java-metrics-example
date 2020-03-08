package example.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * 类型擦除(Type Erasure)：Java的泛型是在编译器层面上实现的（伪泛型）。将代码编译为字节码之后，
 * 不会保存类型信息，所以叫类型擦除。比如所有的List字节码层面都是List。JVM运行的时候只知道这是List。
 * <p>
 * 所以只要在编译的时候绕过了编译器的类型检查，往list里扔啥都行。
 * <p>
 * 擦除后用啥类型？如果没有边界，那Object就是边界。如果是T extends Comparable，那Comparable就是边界。
 * 所以Java引入泛型之后，还能兼容以前的代码，就是因为他们编译后的字节码一个样。。。使用泛型只不过是多了一个编译器校验！
 * <p>
 * 可以看看在{@link ArrayList}的实现里，存放list的数组是Object类型的：transient Object[] elementData;
 * 而且，get操作在取出数据之后，必须有个强制转型的操作：{@link ArrayList#elementData(int)}，(E) elementData[index];
 * 也就是说编译出的字节码里会被插入强制转型操作
 * <p>
 *
 * 没有Arraylist<double>，因为擦除后是Object，Object不能存储double。
 *
 * https://www.cnblogs.com/wuqinglong/p/9456193.html
 *
 * @author puppylpg on 2020/03/08
 */
public class GenericTypeErasure {

    public static void main(String[] args) throws Exception {

        System.out.println("Java泛型：");
        List<Integer> genericList = new ArrayList<>();

        // 泛型api限制，只能添加integer，编译器保证
        genericList.add(1);

        // 绕过编译器类型检查
        genericList.getClass().getMethod("add", Object.class).invoke(genericList, "pikachu");

        // 以Object的类型取数据
        for (Object integer : genericList) {
            System.out.println(integer);
        }

        System.out.println("没泛型的老Java：");
        // 类型擦除后的样子，默认用Object作为边界
        // 所以可以往里面扔各种Object
        // add方法其实相当于add(Object o)
        List oldJavaList = new ArrayList();
        oldJavaList.add(1);
        oldJavaList.add("pikachu");

        for (Object o : oldJavaList) {
            System.out.println(o);
        }

        System.out.println("新老Java混合用：");
        List<Integer> combineButUseNew = new ArrayList();
        // compile error
//        combineButUseNew.add("pikachu");
        combineButUseNew.add(1);
        // 但是能用addAll。从编译警告来看，这属于一次对Collection的强制转型，且编译器无法判定这个强转能不能行，所以就给过了
        // Unchecked assignment: 'java.util.List' to 'java.util.Collection<? extends java.lang.Integer>'
        combineButUseNew.addAll(oldJavaList);
        for (Object o : combineButUseNew) {
            System.out.println(o);
        }

        List combineButUseOld = new ArrayList<Integer>();
        // compile ok
        combineButUseOld.add("pikachu");
        combineButUseOld.addAll(oldJavaList);
        for (Object o : combineButUseOld) {
            System.out.println(o);
        }

        // illegal generic type for instanceof，instanceof是不能加泛型的，因为运行时没有泛型信息了
//        System.out.println(combineButUseOld instanceof List<Integer>);
    }
}
