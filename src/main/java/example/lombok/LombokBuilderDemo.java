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
        Student student = Student.builder()
                .age(5)
                .name("Lily")
                .dreams(Arrays.asList("eat", "sleep", "hitDouDou"))
                .build();

        System.out.println(student);
    }
}