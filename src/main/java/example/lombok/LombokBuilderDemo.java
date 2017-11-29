package example.lombok;

import lombok.Builder;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author liuhaibo on 2017/11/29
 */
public class LombokBuilderDemo {

    @ToString
    @Builder
    private static class Student {
        String name;
        int age;
        List<String> dreams = new ArrayList<>();
    }

    public static void main(String... strings) {
        // generate a builder
        Student.StudentBuilder studentBuilder = Student.builder();

        // set fields
        studentBuilder.age(5).name("Lily");
        studentBuilder.dreams(Arrays.asList("sleep", "eat", "study"));

        // generate object
        Student student = studentBuilder.build();

        System.out.println(student);
    }
}