package example.nio.asyncio;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author liuhaibo on 2018/05/09
 */
public class Client {

    public static void main(String ... args) throws IOException {

        int i = 0;

        while (i++ < 20) {
            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress(6657 + RandomUtils.nextInt(0, 3)));
            String msg = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(20, 40));
            System.out.println(i + " === " + msg);
            sendMessage(sc, msg);
            sc.close();
        }
    }

    private static void sendMessage(SocketChannel socketChannel, String mes) throws IOException {
        if (mes == null || mes.isEmpty()) {
            return;
        }
        byte[] bytes = mes.getBytes(StandardCharsets.UTF_8);
        int size = bytes.length;
        ByteBuffer buffer = ByteBuffer.allocate(size);

        buffer.put(bytes);

        buffer.flip();
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }
}
