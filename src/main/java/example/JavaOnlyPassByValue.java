package example;

import lombok.Data;

import javax.annotation.Nonnull;

/**
 * Java仅值传递。Java有指针，只是不能获取指针的地址，所以没有c中复杂的&取地址之类的，搞得跟没指针、pass-by-reference似的。
 * 其实不是。
 *
 * @author liuhaibo on 2019/01/15
 */
public class JavaOnlyPassByValue {

    public static void main(String... args) {
        // 假设这个狗狗在地址11，则该指针的值就是11。
        // 在Java中，无法获取指针的地址。所以该指针地址不详。
        Dog dog = new Dog("Guilin");
        // 把11这个值传进函数
        unchangeDog(dog);
        // By default, assertions are disabled.
        // The syntax for enabling assertion statement in Java source code is:
        // java –ea XXX, or java –enableassertions XXX
        // 该指针的地址依旧是11，获取的依旧是在11的狗狗
        assert dog.getName().equals("Guilin");
        System.out.println(dog.getName());
        changeDog(dog);
        assert dog.getName().equals("Haibo");
        System.out.println(dog.getName());
    }

    // 新的指针的值为11，同样新指针地址不详
    private static void unchangeDog(Dog dog) {
        // （该指针的值刚通过函数调用赋值为11，一进来就被改成了22，所以IDE提示：Parameter can be converted to a local variable）

        // 假设这个狗狗在地址22，则11这个值被替换成了22
        // 所以新指针的值为22
        dog = new Dog("Haibo");
        // 22这个地方的狗狗改名为皮卡丘
        dog.setName("pikachu");
    }

    private static void changeDog(Dog dog) {
        dog.setName("Haibo");
    }

    @Data
    private static class Dog {
        @Nonnull
        private String name;
    }
}
