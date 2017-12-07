package example.others;

/**
 * @author liuhaibo on 2017/12/07
 */
public class StringDemo {

    public static void main(String[] args) {
        String str1 = "hello";
        String str2 = "hello";
        // rarely used
        String str3 = new String("hello");

        // str1 & str2 are the same immutable string from "string constant poll"
        System.out.println(str1.equals(str2));  // true
        System.out.println(str1 == str2);       // true

        // str3 is just a copy of "hello",
        // so they are just same in content, but are not the same object
        System.out.println(str1.equals(str3));  // true
        System.out.println(str1 == str3);       // false
    }
}