package example.url;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * URL is a subset of URI.
 * <p>
 * URI: identifier，资源的标志符，仅用于资源标记，比如tel:+1-816-555-1212、urn:isbn:1234567890等；
 * URL: locator，资源的定位符，需要提供资源访问。所以它的scheme必须是某种protocol，protocol提供了具体的访问细节。
 *
 * <p>
 * URL是远程资源的有效引用，所以可以通过{@link URL#openStream()}连接资源获取其内容。而URI只是一个标志。
 * 详见{@link UrlDemo}
 * <p>
 * URI: scheme:[//authority][/path][?query][#fragment]
 * <p>
 * scheme: if it's URL, scheme must be protocol used to access the resources.
 * If it's URI, scheme is just a identifier.
 * <p>
 * 实例化URL的时候，要根据protocol获取streamHandler（getURLStreamHandler），比如http的就是sun.net.www.protocol.http.Handler，
 * 进而获取HttpURLConnection等。获取handler的方式就是sun.net.www.protocol+scheme，如果scheme不是Java支持的protocol，自然没有相应的package，
 * 就找不到类，无法实例化。所以创建URL出错。
 *
 * @author liuhaibo on 2019/09/27
 */
public class URLURI {

    public static void main(String... args) throws URISyntaxException, MalformedURLException {
        URI firstURI = new URI(
                "somescheme://theuser:thepassword@someauthority:80/some/path?thequery#somefragment"
        );

        URI secondURI = new URI(
                "somescheme",
                "theuser:thepassword",
                "someuthority",
                80,
                "/some/path",
                "thequery",
                "somefragment"
        );

        URL firstURL = new URL(
                "http://theuser:thepassword@somehost:80/path/to/file?thequery#somefragment"
        );
        URL secondURL = new URL("http", "somehost", 80, "/path/to/file");

        URI uri = URI.create("urn:isbn:1234567890");

        // scheme不是protocol，不能实例化为URL
        try {
            URL theURL = new URL("otherprotocol://somehost/path/to/file");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        convert();
        convertFail();

    }

    /**
     * scheme是protocol的URI可以转成URL。
     */
    private static void convert() throws URISyntaxException, MalformedURLException {
        String aURIString = "http://somehost:80/path?thequery";
        URI uri = new URI(aURIString);
        URL url = new URL(aURIString);

        URL toURL = uri.toURL();
        URI toURI = url.toURI();
    }

    /**
     * 非URL（scheme不是protocol）的URI不能转成URL。
     */
    private static void convertFail() throws URISyntaxException {
        URI uri = new URI("somescheme://someauthority/path?thequery");

        try {
            URL url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
