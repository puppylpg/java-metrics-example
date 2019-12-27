package example.others;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 不完全总结如下：
 *
 * 配置以冒号、等号、空格作为k-v的分界符，一行中如果有多个，以第一个为准，其他都作为普通字符；
 * 如果key中本身就含有上述分界符，需要用转义符号'\'转义；
 * 支持#、!开头，作为comment，comment行会被忽略；
 * 空白行会被忽略；
 * 行首的空白符会被忽略，行尾的不会；
 * 分界符前后的空白符也会被忽略；
 * 配置跨行时，使用'\'，下一行的内容相当于直接替换了这一行的'\'（所以下一行就算是#/!开头，也不会作为注释）；
 * 不支持汉字，只支持 ISO 8859-1 字符集；
 *
 * @author liuhaibo on 2019/12/25
 */
public class PropertyLoadDemo {

    public static void main(String... args) throws IOException {
        InputStream input = PropertyLoadDemo.class.getClassLoader().getResourceAsStream("property_load_demo.txt");
        Properties properties = new Properties();
        properties.load(input);
        properties.forEach((k, v) -> System.out.println(k + "<--->" + v));
    }
}
