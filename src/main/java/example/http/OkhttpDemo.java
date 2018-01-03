package example.http;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

/**
 * @author liuhaibo on 2018/01/03
 */
public class OkhttpDemo {

    private static String prefix = "http://gorgon.youdao.com/gorgon/request.s?";
    private static String url = prefix + "id=b6de42029629f42d99be81373ff0bc87";

    private static String githubRest = "https://api.github.com/users/puppylpg";

    public static void main(String[] args) throws IOException {
        OkhttpDemo demo = new OkhttpDemo();
        System.out.println("\nDemo url: ");
        demo.printUrl();
        System.out.println("\nSynchronous Get: ");
        demo.synchronousGet(url);
        System.out.println("\nSynchronous Get(generated url): ");
        demo.synchronousGet(generateURL(prefix).toString());
        System.out.println("\nAsynchronous Get: ");
        demo.asynchronousGet();
        System.out.println("\nPOST: ");
        demo.doPost();
        System.out.println("\ncached Get: ");
        demo.cachedGet();
    }

    private void synchronousGet(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Request{method=GET, url=http://gorgon.youdao.com/gorgon/request.s?id=b6de42029629f42d99be81373ff0bc87, tag=null}
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);

        // Response{protocol=http/1.1, code=200, message=OK, url=http://gorgon.youdao.com/gorgon/request.s?id=b6de42029629f42d99be81373ff0bc87}
        Response response = call.execute();
        ResponseBody responseBody = response.body();

        System.out.println(responseBody.string());
    }

    /**
     * add parameter
     *
     * @param prefix url prefix
     * @return full url
     */
    private static HttpUrl generateURL(String prefix) {
        HttpUrl httpUrl = HttpUrl.parse(prefix).newBuilder()
                .addQueryParameter("id", "b6de42029629f42d99be81373ff0bc87")
                .build();
        return httpUrl;
    }

    /**
     * Sample of creating url.
     * <p>
     * https://www.google.com/search?q=hello%20world
     */
    private void printUrl() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("www.google.com")
                .addPathSegment("search")
                .addQueryParameter("q", "hello world")
                .build();
        System.out.println(url);
    }

    /**
     * Asynchronous
     *
     * @throws IOException
     */
    private void asynchronousGet() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(githubRest).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    System.out.println(response.body().string());
                }
            }
        });
    }

    private void doPost() throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody form = new FormBody.Builder()
                .add("username", "admin")
                .add("password", "admin")
                .build();

        Request request = new Request.Builder()
                .url("https://api.github.com/users")
                .post(form)
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }

    private void cachedGet() throws IOException {
        int cacheSize = 10 * 1024 * 1024;

        File cacheDirectory = new File("src/test/resources/cache");

        Cache cache = new Cache(cacheDirectory, cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        Response first = client.newCall(request).execute();
        System.out.println("chacheResponse(): "  + first.cacheResponse());
        System.out.println("networkResponse(): "  + first.networkResponse());
        System.out.println("response content: "  + first.body().string());

        Response second = client.newCall(request).execute();
        System.out.println("chacheResponse(): "  + second.cacheResponse());
        System.out.println("networkResponse(): "  + second.networkResponse());
        System.out.println("response content: "  + second.body().string());
    }
}