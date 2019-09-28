package example.url;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.jar.Manifest;

/**
 * （我算是看出来了，涉及到底层的东西：open socket，write磁盘，flush输出流之类的东西，
 * 具体细节都是底层实现的，可能已经不是Java写的了，看源码也看不出什么鬼。这不是应该知道的。
 * 应该知道的是：为什么flush，什么时候该flush，从“逻辑上”而非物理上来讲，这些都改怎么操作，知道这些就行了！）
 *
 * 使用URL打开一个互联网资源，逻辑上像打开个本地文件一样（这才是应该知道的，“逻辑上像打开个本地文件一样”），步骤为：
 * 一个URL -> openStream，打开读取的流 -> 读出数据，选一个输出方式输出 -> done.
 * 逻辑上来看，这跟打开一个文件没啥区别。因为打开流这个过程隐藏了很多实现细节：
 *
 * URLStreamHandler -> http.handler -> openConnection()（并没有open，只是创建了一个HttpUrlConnection） -> getInputStream() -> connect() -> 对client getOutputStream -> writeRequest（ua为Java/1.8.0_191）到httpClient -> parse response -> httpClient.getInputStream()和response code
 * 每个URL都有一个URLStreamHandler，像http开头（协议为http）的资源的URL就是一个sun的包下的http.handler。
 * URL openStream实际上就是：
 * 1. handler先openConnection（实际上是创建Connection，只是返回了一个HttpUrlConnection，并没有真连）；
 * 2. handler把connection connect，因为是http的，所以创建了一个HttpClient，这里用的是sun.net.www.http.HttpClient；
 * 3. 从client读outputStream，创建一个request，一大堆细节blabla，再解析response；
 * 4. 最后从client getInputStream，从而取到了response的stream，可以读结果了。
 *
 * 所以你看，逻辑上是读了一个http的资源，实际上内部屏蔽了许多细节，使读取URL资源看起来就是读一下。
 *
 * （我现在一看到URL就想的是http资源，这是不对的。URL就是一个统一资源定位器，是一个抽象的东西。http只是一种，jar，hdfs，本地文件等都是）
 * 所以现在再看很多资源都返回url一点儿都不奇怪了。url是对资源的抽象，not equals to http url
 */
public class UrlDemo {

    public static void main(String... args) throws IOException {

        // 写到file
//        File file = new File(".../baidu.html");
//        PrintWriter printWriter = new PrintWriter(file);
        // 写到stdout
        PrintWriter printWriter = new PrintWriter(System.out);

        // 对url的统一操作，不管它是一个网络资源还是本地文件资源，
        // 只要搞成url了，就没区别了。这就是url抽象的作用。
        httpProtocolURL(printWriter);
        fileProtocolURL(printWriter);
        classpathFileURL(printWriter);
        jarFileProtocolURL(printWriter);

        // System.out只能关一次，所以我只能这么写了
        printWriter.close();
    }

    private static void httpProtocolURL(PrintWriter printWriter) throws IOException {
        URL url = new URL("http://www.baidu.com");
        readURL(url, printWriter);
    }

    /**
     * file protocol 的handler，FileURLConnection在connect的时候，用的是：
     * this.inputStream = new BufferedInputStream(new FileInputStream(this.filename))
     * 它已经得到InputStream，只不过封装进了URL里。而我们用URL的时候，只不过再取出InputStream
     */
    private static void fileProtocolURL(PrintWriter printWriter) throws IOException {
        // https://en.wikipedia.org/wiki/File_URI_scheme
        // file://host/path
        // file:///path (omit host = localhost)
        // file:/path (omit host = localhost)
        // Windows eg: file:///f:/Documents/Crawler/Crawler.py，写盘符的时候多加个冒号而已
        URL url = new URL("file://localhost/etc/hosts");
        readURL(url, printWriter);
    }

    private static void classpathFileURL(PrintWriter printWriter) throws IOException {
        URL url = UrlDemo.class.getClassLoader().getResource("log4j.xml");
        readURL(url, printWriter);
    }

    private static void jarFileProtocolURL(PrintWriter printWriter) throws IOException {
        // JAR URL is `jar:<url>!/{entry}`
        URL url = new URL("jar:file:///C:/Users/puppylpg/Codes/java-examples/target/java-examples-1.0-SNAPSHOT.jar!/");
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        Manifest manifest = connection.getManifest();
        printWriter.println(manifest);
    }

    private static void readURL(URL url, PrintWriter printWriter) throws IOException {
        // use openStream() to get a InputStream
        InputStream inputStream = url.openStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String s = bufferedReader.readLine();
        while (s != null) {
            printWriter.println(s);
            printWriter.flush();
            s = bufferedReader.readLine();
        }

        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        // 另外一个建议：不要写“不对称”的代码！
        // PrintWriter不是在这里创建的，也不要在这里关闭！
        // 关闭流会同时关闭它包裹的流，且不能重新打开！
        // 万一里面包的是静态的System.out，凉凉，后面再调用这个函数，再也无法输出了！
    }
}
