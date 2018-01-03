package example.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpURLConnection conn = (HttpURLConnection) u.openConnection();
 * only creates an Object.
 * <p>
 * connect() method can be invoked by conn.getInputStream();
 * <p>
 * You are not always required to explicitly call the connect method to initiate the connection.
 * <p>
 * Operations that depend on being connected, like getInputStream, getOutputStream, etc, will implicitly perform the connection, if necessary.
 *
 * @author liuhaibo on 2018/01/03
 */
public class OriginalHttp {

    private static String prefix = "http://gorgon.youdao.com/gorgon/request.s?";
    private static String url = prefix + "id=b6de42029629f42d99be81373ff0bc87";
    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        OriginalHttp http = new OriginalHttp();

        System.out.println("Testing 1 - Send Http GET request");
        http.sendGet();

        System.out.println("\nTesting 2 - Send Http POST request");
        http.sendPost();
    }

    /**
     * HTTP GET request
     */
    private void sendGet() throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response);

    }

    /**
     * HTTP POST request
     */
    private void sendPost() throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response);

    }
}