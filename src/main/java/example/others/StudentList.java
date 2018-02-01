package example.others;

import java.util.*;

/**
 * 实现{@link Iterable}接口，是为了能直接for循环遍历其中的变量T。
 *
 * @author liuhaibo on 2018/02/01
 */
public class StudentList implements Iterable<Student> {

    private final List<Student> students = new ArrayList<Student>(){
        {
            add(new Student("xiaoMing", 12));
            add(new Student("xiaoLi", 23));
        }
    };

    /**
     * 不用这个iterator，如果每次都重新获取（如{@link #iterator}方法的hashNext被注掉的代码所示），
     * 会永远只输出第一个元素。。。
     */
    private Iterator<Student> iterator = students.iterator();

    /**
     * 普遍做法
     *
     * @return {@link  #students#iterator()}
     */
//    @Override
//    public Iterator<Student> iterator() {
//        return students.iterator();
//    }

    /**
     * 跟上面一样的做法，这样写只不过为了熟悉一下{@link Iterator}。
     *
     * @return
     */
    @Override
    public Iterator<Student> iterator() {
        return new Iterator<Student>() {
            @Override
            public boolean hasNext() {
//                return students.iterator().hasNext();
                return iterator.hasNext();
            }

            @Override
            public Student next() {
//                return students.iterator().next();
                return iterator.next();
            }

            /**
             * this is the default implementation in {@link Iterator}
             */
            @Override
            public void remove() {
                throw new UnsupportedOperationException("no changes allowed");
            }
        };
    }

    /**
     * 很好玩的现象，揭露深层信息：
     * 1. 使用上面注掉的两行代码，每次获取新的iterator，则一直输出第一个元素：因为每次的都是新iterator，所以每次hasNext/next都是第一个元素；
     * 2. next()返回新的iter，hasNext()使用{@link #iterator}的next()，依旧一直输出第一个：
     *      因为next()每次都消耗新iter的第一个元素，不消耗{@link #iterator}的第二个元素，所以hasNext()会一直判断有第二个元素；
     * 3. hasNext()返回新的iter，next()使用{@link #iterator}的next()，则输出所有元素之后就抛异常NoSuchElementException，
     *      有了第二个现象就不难理解这个了：hasNext()每次都判断新的iter有下一个（第一个）元素，next()一直消耗的是{@link #iterator}的元素，消耗完了还下一个，就抛异常了。
     *
     * 只有用自己的iterator的hashNext和next，才不会出事儿。
     *
     * @param args
     */
    public static void main(String[] args) {
        StudentList studentList = new StudentList();
        for (Student student : studentList) {
            System.out.println(student.say());
        }
    }
}