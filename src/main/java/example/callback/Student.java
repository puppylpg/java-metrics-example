package example.callback;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liuhaibo on 2018/04/12
 */
@AllArgsConstructor
public class Student {

    @Getter
    private String name;
    @Getter
    private int age;

    private String animal;

    public void think(IListener listener, String question) {
        System.out.println(name + " is thinking... " + question);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(name + ": My answer is " + animal + ".");
        listener.evaluate(animal, name);
    }
}
