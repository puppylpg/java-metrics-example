package example.fileio;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author liuhaibo on 2018/01/09
 */
public class FileIODemo {

    private static final String FILE = "config/example.properties";

    public static void main(String[] args) throws IOException {
        System.out.println("\nRead Text File: FileReader(+BufferedReader)");
        readTextFile();
        System.out.println("\nRead Byte File: FileInputStream");
        readBinaryFile();

        System.out.println("\nWrite Text File: FileWriter");
        writeTextFile();
        System.out.println("\nWrite Byte File: FileOutputStream");
        writeBinaryFile();
    }

    /**
     * Use FileReader for text file.
     * <p>
     * Always wrap FileReader with BufferedReader
     * <p>
     * Without buffering, each
     * invocation of read() or readLine() could cause bytes to be read from the
     * file, converted into characters, and then returned, which can be very
     * inefficient.
     *
     * @throws IOException
     */
    private static void readTextFile() throws IOException {
        FileReader fr = new FileReader(FILE);
        // always wrap FileReader in BufferedReader
        BufferedReader br = new BufferedReader(fr);

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        // always close files
        br.close();
    }

    /**
     * If you want to read a binary file, or a text file containing 'weird' characters
     * (ones that your system doesn't deal with by default), you need to use FileInputStream
     * instead of FileReader.
     * <p>
     * Instead of wrapping FileInputStream in a buffer,
     * FileInputStream defines a method called read() that lets you fill a buffer with data.
     *
     * @throws IOException
     */
    private static void readBinaryFile() throws IOException {
        // 10 is apparently too small, but it's useful to see the output
        // 其实在重复使用之前，缓冲区应该先被清空的
        byte[] buffer = new byte[10];

        FileInputStream fis = new FileInputStream(FILE);

        int totalBytes = 0, readBytes = 0;
        while ((readBytes = fis.read(buffer)) != -1) {
            // if you are really reading a binary file(image/bin),
            // you'll not convent it to a string and print it to the console!
            System.out.println(new String(buffer));
            // ged:2,clic
            // k_sdk_char
            // ged:2_char
            // 最后一次打印输出“ged:2”就该结束了，但是因为没有清空buffer
            // 所以最后打印出了“ged:2_char”，其中“_char”是上次读取的数据。
            totalBytes += readBytes;
        }

        fis.close();
        System.out.println("Totally " + totalBytes + " bytes.");
    }

    /**
     * pichu@Archer ~ $ cat /tmp/hello.txt
     * hello
     * 世界=.=%
     *
     * @throws IOException
     */
    private static void writeTextFile() throws IOException {
        FileWriter fileWriter = new FileWriter("/tmp/hello.txt");
        BufferedWriter bw = new BufferedWriter(fileWriter);

        bw.write("hello");
        bw.newLine();
        bw.write("世界=.=");

        bw.close();
    }

    /**
     * 写的是字节，在翻译成系统默认的UTF-8时，乱码了：
     *
     * pichu@Archer ~ $ cat /tmp/world
     * ��hello, NuL. I love you~%
     *
     * @throws IOException
     */
    private static void writeBinaryFile() throws IOException {
        FileOutputStream fos = new FileOutputStream("/tmp/world");

        String str = "hello, 世界. I love you~";
        byte[] bytes = str.getBytes(StandardCharsets.UTF_16);

        fos.write(bytes);

        fos.close();
    }
}