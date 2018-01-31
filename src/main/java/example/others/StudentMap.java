package example.others;

import java.util.HashMap;

/**
 * 伪typedef（psuedo-typedef）：用来模拟c中的typedef的，碰到的时候完全可以理解为typedef，就当是eg: HashMap<String, Student>去处理。
 *
 * 微不足道的优点：
 * 最初目的是泛型定义一个变量要写两遍，写起来麻烦，但是后来的JDK已经不存在这个问题了。
 * 另外还有一个好处就是名字也许听起来清晰些，但这都是微不足道的好处。
 *
 * 致命的缺点：
 * 1. StringList extends ArrayList<String>和NameList extends ArrayList<String>并不能互通，这点完全不同于c的typedef；
 * 2. 同样是互通问题：StringList能用在参数是ArrayList<String>的地方，以为前者是后者的儿子，但是反之就不行了；
 * 3. StringList和NameList如果分别在两个依赖包里，共用的时候，一个只能写全路径以示区分。且，StringList中的sortList(StringList)方法，NameList不能用；
 * 4. 不可重用，既然继承了ArrayList，那么当需要使用LinkedList的时候，StringList就无法重用了。
 *
 * @author liuhaibo on 2018/01/31
 */
@Deprecated
public class StudentMap extends HashMap<String, Student> {

    StudentMap() {
        super(5);
    }

    public static void main(String[] args) {

        Student xiaoMing = new Student("XiaoMing", 20);
        Student xiaoHong = new Student("XiaoHing", 12);


        StudentMap studentMap = new StudentMap();
        studentMap.put(xiaoMing.name, xiaoMing);
        studentMap.put(xiaoHong.name, xiaoHong);

        studentMap.forEach((x, y) -> System.out.println(x + ": " + y.say()));
    }
}

class Student {

    String name;

    int age;

    Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String say() {
        return String.format("I am %s with age %d.", name, age);
    }
}