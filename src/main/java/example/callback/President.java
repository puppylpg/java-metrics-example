package example.callback;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * 和老师相比，自己没有实现回调，而是直接传入了一个回调实例。
 *
 * @author liuhaibo on 2018/04/12
 */
public class President {

    private String name;
    @Setter
    @Getter
    private ArrayList<Student> student = new ArrayList<>();


    President(String name) {
        this.name = name;
    }

    private void ask() {
        if (student.isEmpty()) {
            System.out.println(name + ": Sad, no one to ask!");
        }
        String question = "An animal!";
        student.forEach( student -> {
            System.out.println(name + ": Ok, please give me a word, " + student.getName() + ". " + question);
            // 传入一个回调实例（当然如果回调是一次性的，也可使用匿名类或者lambda）
            student.think(new Listener(), question);
        });
    }

    /**
     * 内部类，实现回调接口。
     *
     * 这里要用到外部类的非静态变量，所以使用了内部类。而没有使用静态嵌套类。
     * 什么是嵌套？嵌套就是我跟你没关系，自己可以完全独立存在，但是我就想借你的壳用一下，来隐藏一下我自己（真TM猥琐）。
     * 什么是内部？内部就是我是你的一部分，我了解你，我知道你的全部，没有你就没有我。（所以内部类对象是以外部类对象存在为前提的）
     */
    private class Listener implements IListener {

        @Override
        public void evaluate(String answer, String name) {
            // 这里要访问外部类的非静态变量，因此是非静态内部类
            System.out.println(President.this.name + ": Really! Pretty Job " + name);
            System.out.println("----- -----");
        }
    }

    public static void main(String ... args) throws InterruptedException {
        Student tom = new Student("Tom", 12, "Pikachu");
        Student mary = new Student("Mary", 12, "Tomcat");
        Student lucy = new Student("Lucy", 12, "Jerry");
        President president = new President("President Xi");
        president.getStudent().add(tom);
        president.getStudent().add(mary);
        president.getStudent().add(lucy);

        president.ask();
    }
}
