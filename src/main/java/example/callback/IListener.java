package example.callback;

/**
 * @author liuhaibo on 2018/04/11
 */
public interface IListener {

    /**
     * 当学生知道答案的时候，将答案和自己的名字告诉倾听者，而倾听者则给出评价。
     * @param answer 答案
     * @param name 自己的姓名
     */
    void evaluate(String answer, String name);
}
