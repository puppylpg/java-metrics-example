package example.serialize;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author liuhaibo on 2017/11/30
 */
public class SerializeDemo {

    /**
     * 1. must implements Serializable;
     * 2. transient field can't be serialized.
     */
    @AllArgsConstructor
    @ToString
    private static class Student implements Serializable {
        String name;
        int age;
        /**
         * transient can not be Serialized
         */
        transient String think;
        List<String> dreams;
    }

    @AllArgsConstructor
    @ToString
    private static class Teacher implements Serializable {
        String gender;
        int salary;
    }

    public static void main(String... strings) {
        Student inputStudent = new Student("Lily", 18, "nothing", Arrays.asList("eat", "play"));
        Teacher inputTeacher = new Teacher("female", 10000);

        System.out.println(inputStudent);
        System.out.println(inputTeacher);

        String location = "/tmp/people.ser";

        try {
            FileOutputStream fos = new FileOutputStream(location);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(inputStudent);
            oos.writeObject("liuhaibo");
            oos.writeObject(inputTeacher);

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fis = new FileInputStream(location);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // readObject() -> ClassNotFoundExcception
            // For a JVM to be able to deserialize an object,
            // it must be able to find the bytecode for the class
            Student resStudent = (Student)ois.readObject();
            String myName = (String)ois.readObject();
            Teacher resTeacher = (Teacher)ois.readObject();

            // if `readObject -> myName` and `readObject -> resTeacher` are reversed:
            // java.lang.ClassCastException: java.lang.String cannot be cast to example.serialize.SerializeDemo$Teacher

            // SerializeDemo.Student(name=Lily, age=18, think=null, dreams=[eat, play])
            System.out.println(resStudent);
            System.out.println(myName);
            System.out.println(resTeacher);

            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}