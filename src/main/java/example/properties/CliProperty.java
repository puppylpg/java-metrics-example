package example.properties;

/**
 * @author liuhaibo on 2018/07/09
 */
public class CliProperty {

    /**
     * 1. vm options:    -Dliuhaibo.love=liuguilin-FOREVER
     * 2. java -Dliuhaibo.love=liuguilin-forever -cp target/examples-1.0-SNAPSHOT.jar example.properties.CliProperty
     *
     * -Dproperty=value
     *      Sets a system property value.
     *      The property variable is a string with no spaces that represents the name of the property.
     *      The value variable is a string that represents the value of the property.
     *      If value is a string with spaces, then enclose it in quotation marks (for example -Dfoo="foo bar").
     *
     * @param args args
     */
    public static void main(String ... args) {
        System.out.println(System.getProperty("liuhaibo.love"));
    }
}
