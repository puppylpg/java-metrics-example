package example.nio.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author puppylpg on 2021/03/30
 */
public class AsyncClient {

    public static void main(String... args) throws IOException, ExecutionException, InterruptedException {

        Client client = new Client();

        String resp1 = client.sendMessage("hello");
        String resp2 = client.sendMessage("world");
        System.out.println(resp1);
        System.out.println(resp2);
    }

    public static class Client {

        AsynchronousSocketChannel client;

        Client() throws IOException, ExecutionException, InterruptedException {
            client = AsynchronousSocketChannel.open();
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4999);
            Future<Void> future = client.connect(hostAddress);

            future.get();
        }

        String sendMessage(String message) throws ExecutionException, InterruptedException {
            byte[] byteMsg = new String(message).getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.wrap(byteMsg);
            Future<Integer> writeResult = client.write(buffer);

            // do some computation

            writeResult.get();
            buffer.flip();
            Future<Integer> readResult = client.read(buffer);

            // do some computation

            readResult.get();
            String echo = new String(buffer.array()).trim();
            buffer.clear();
            return echo;
        }
    }


}
