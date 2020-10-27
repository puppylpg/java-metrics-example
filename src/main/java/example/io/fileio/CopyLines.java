// hello皮卡丘
package example.io.fileio;

/**
 * @author puppylpg on 2020/10/28
 */
import java.io.*;
import java.nio.charset.StandardCharsets;

public class CopyLines {
    public static void main(String[] args) throws IOException {

        BufferedReader inputStream = null;
        PrintWriter outputStream = null;

        try {
            // TODO: Java没有带charset的FileReader构造函数
            // inputStream = new BufferedReader(new FileReader("src/main/java/example/io/fileio/CopyLines.java", StandardCharsets.UTF_8));
            // TODO: 如果用US_ASCII读该文件（以utf-8保存），文件里的汉字就不能被正确读取并转为char。字母可以，两个字符集的ascii字母通用
            // TODO: 如果用UTF-16读该文件，凉凉，每一个字符能读对的
            inputStream = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/java/example/io/fileio/CopyLines.java"), StandardCharsets.UTF_16));
            outputStream = new PrintWriter(new FileWriter("target/CopyLines.txt"));

            String l;
            while ((l = inputStream.readLine()) != null) {
                outputStream.println(l);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}

