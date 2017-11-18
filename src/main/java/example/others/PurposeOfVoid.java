package example.others;

/**
 * 1. in reflection: get the return type of a method as void;
 * 2. use Void class in Generics to specify that
 *      you don't care about the specific type of object being used.
 *      For example: List<void> list1;
 *
 * @author liuhaibo on 2017/11/19
 */
public class PurposeOfVoid {

    public static void main(String[] args) throws SecurityException, NoSuchMethodException {
        Class clazz = Test.class.getMethod("test",null).getReturnType();
        System.out.println(clazz == Void.TYPE);
        System.out.println(clazz == Void.class);
    }

    class Test{
        public void test(){}
    }
}
