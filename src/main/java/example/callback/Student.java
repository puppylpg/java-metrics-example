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

    /**
     * 带回调参数的方法，完成之后要调用回调以给出反馈结果。
     * @param listener 回调接口
     * @param question
     */
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
