package example.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ScatterGatherChannel {
    static private final int firstHeaderLength = 2;
    static private final int secondHeaderLength = 4;
    static private final int bodyLength = 6;

    static public void main(String args[]) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java ScatterGatherChannel port");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        InetSocketAddress address = new InetSocketAddress(port);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(address);

        int messageLength = firstHeaderLength + secondHeaderLength + bodyLength;

        ByteBuffer buffers[] = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate(firstHeaderLength);
        buffers[1] = ByteBuffer.allocate(secondHeaderLength);
        buffers[2] = ByteBuffer.allocate(bodyLength);

        // it's a ScatteringByteChannel & GatheringByteChannel
        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true) {

            // Scatter-read into buffers
            int bytesRead = 0;
            while (bytesRead < messageLength) {
                long r = socketChannel.read(buffers);
                bytesRead += r;

                System.out.println("r " + r);
                for (int i = 0; i < buffers.length; ++i) {
                    ByteBuffer bb = buffers[i];
                    System.out.println("b " + i + " " + bb.position() + " " + bb.limit());
                }
            }

            // Process message here
            for (ByteBuffer buffer : buffers) {
                System.out.println(buffer);
            }

            // Flip buffers
            for (int i = 0; i < buffers.length; ++i) {
                ByteBuffer bb = buffers[i];
                bb.flip();
            }

            // Scatter-write back out
            long bytesWritten = 0;
            while (bytesWritten < messageLength) {
                long r = socketChannel.write(buffers);
                bytesWritten += r;
            }

            // Clear buffers
            for (int i = 0; i < buffers.length; ++i) {
                ByteBuffer bb = buffers[i];
                bb.clear();
            }

            System.out.println(bytesRead + " " + bytesWritten + " " + messageLength);
        }
    }
}
