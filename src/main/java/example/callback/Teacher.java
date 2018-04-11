package example.callback;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * 回调的核心就是回调方将本身即this传递给调用方（作为调用方的函数的参数），这样调用方就可以在调用完毕之后告诉回调方它想要知道的信息。
 *
 * 老师是需要被回调的对象，因为他需要学生在想出来之后告诉他答案。
 *
 * @author liuhaibo on 2018/04/11
 */
public class Teacher implements IListener {

    private String name;
    @Setter
    @Getter
    private ArrayList<Student> student = new ArrayList<>();


    Teacher(String name) {
        this.name = name;
    }

    private void ask() {
        if (student.isEmpty()) {
            System.out.println(name + ": Sad, no one to ask!");
        }
        String question = "An animal!";
        student.forEach( student -> {
            System.out.println(name + ": Ok, please give me a word, " + student.getName() + ". " + question);
            student.think(this, question);
        });
    }

    private void askAsync() {
        if (student.isEmpty()) {
            System.out.println(name + ": Sad, no one to ask!");
        }
        String question = "An animal!";
        student.forEach(student ->
                new Thread(
                        // lambda表达式代替匿名内部类Runnable
                        () -> {
                            System.out.println(name + ": Ok, please give me a word, " + student.getName() + ". " + question);
                            student.think(Teacher.this, question);
                        }
                ).start()
        );
    }

    @Override
    public void evaluate(String answer, String name) {
        System.out.println(this.name + ": " + name + ", is it " + answer + " ?");
        System.out.println("----- -----");
    }

    public static void main(String ... args) throws InterruptedException {
        Student tom = new Student("Tom", 12, "Pikachu");
        Student mary = new Student("Mary", 12, "Tomcat");
        Student lucy = new Student("Lucy", 12, "Jerry");
        Teacher teacher = new Teacher("Mr. Li");
        teacher.getStudent().add(tom);
        teacher.getStudent().add(mary);
        teacher.getStudent().add(lucy);

        System.out.println("同步问：");
        teacher.ask();
        System.out.println("异步问：");
        teacher.askAsync();
    }

    /*
    同步问：
    Mr. Li: Ok, please give me a word, Tom. An animal!
    Tom is thinking... An animal!
    Tom: My answer is Pikachu.
    Mr. Li: Tom, is it Pikachu ?
    ----- -----
    Mr. Li: Ok, please give me a word, Mary. An animal!
    Mary is thinking... An animal!
    Mary: My answer is Tomcat.
    Mr. Li: Mary, is it Tomcat ?
    ----- -----
    Mr. Li: Ok, please give me a word, Lucy. An animal!
    Lucy is thinking... An animal!
    Lucy: My answer is Jerry.
    Mr. Li: Lucy, is it Jerry ?
    ----- -----
    异步问：
    Mr. Li: Ok, please give me a word, Tom. An animal!
    Tom is thinking... An animal!
    Mr. Li: Ok, please give me a word, Mary. An animal!
    Mary is thinking... An animal!
    Mr. Li: Ok, please give me a word, Lucy. An animal!
    Lucy is thinking... An animal!
    Tom: My answer is Pikachu.
    Mary: My answer is Tomcat.
    Mr. Li: Mary, is it Tomcat ?
    ----- -----
    Mr. Li: Tom, is it Pikachu ?
    ----- -----
    Lucy: My answer is Jerry.
    Mr. Li: Lucy, is it Jerry ?
    ----- -----
    */
}
