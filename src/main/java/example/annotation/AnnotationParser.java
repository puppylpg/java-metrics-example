package example.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * annotation一定由三部分组成：
 * 1. annotation定义；
 * 2. annotation使用者；
 * 3. annotation解析者；
 *
 * 一般jdk或者第三方（比如spring）的annotation的定义和解析都被隐藏了。用户只要会使用就行了。
 *
 * 解析策略：
 * 1. 读取源代码文本，逐字符解析？太原始太困难了，不可取；
 * 2. 从反射读取annotation？yes！
 */
public class AnnotationParser {

    public static void main(String... args) {
        Class<AnnotationDemo> clazz = AnnotationDemo.class;
        Pikachu annotationForClass = clazz.getAnnotation(Pikachu.class);
        //class:
        //value() annotation: pikachu
        //speak() annotation: pikapika on class
        System.out.println("class:");
        printAnnotation(annotationForClass);

        //field: hello
        //value() annotation: pikachu on field 'hello'
        //speak() annotation: pika
        //field: world
        //value() annotation: pikachu on field 'world'
        //speak() annotation: pikapika on field 'world'
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("field: " + field.getName());
            Pikachu[] annotations = field.getAnnotationsByType(Pikachu.class);
            for (Pikachu annotation : annotations) {
                printAnnotation(annotation);
            }
        }

        //method: say
        //value() annotation: pikachu
        //speak() annotation: pika
        //method: wait
        //method: wait
        //method: wait
        //method: equals
        //method: toString
        //method: hashCode
        //method: getClass
        //method: notify
        //method: notifyAll
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            System.out.println("method: " + method.getName());
            Pikachu[] annotations = method.getAnnotationsByType(Pikachu.class);
            for (Pikachu annotation : annotations) {
                printAnnotation(annotation);
            }
        }
    }

    private static void printAnnotation(Pikachu pikachu) {
        System.out.println("value() annotation: " + pikachu.value());
        System.out.println("speak() annotation: " + pikachu.speak());
    }
}
