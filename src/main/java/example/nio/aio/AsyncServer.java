package example.nio.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author puppylpg on 2021/03/30
 */
public class AsyncServer {

    public static void main(String... args) throws IOException {
        AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4999);
        serverChannel.bind(hostAddress);

        while (true) {
            serverChannel.accept(
                    null,
                    new CompletionHandler<AsynchronousSocketChannel, Object>() {

                        @Override
                        public void completed(AsynchronousSocketChannel clientChannel, Object attachment) {
                            if (serverChannel.isOpen()) {
                                serverChannel.accept(null, this);
                            }

                            try {
                                System.out.println(String.format("[%s] client connected: %s", Thread.currentThread().getName(), clientChannel.getRemoteAddress()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (clientChannel.isOpen()) {
                                ReadWriteHandler handler = new ReadWriteHandler();
                                ByteBuffer buffer = ByteBuffer.allocate(32);

                                Map<String, Object> readInfo = new HashMap<>();
                                readInfo.put("action", "read");
                                readInfo.put("buffer", buffer);
                                readInfo.put("channel", clientChannel);

                                clientChannel.read(buffer, readInfo, handler);
                            }
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            // process error
                        }
                    });
            System.in.read();
        }
    }

    public static class ReadWriteHandler implements CompletionHandler<Integer, Map<String, Object>> {

        @Override
        public void completed(Integer client, Map<String, Object> attachment) {
            String action = (String) attachment.get("action");

            if ("read".equals(action)) {
                ByteBuffer buffer = (ByteBuffer) attachment.get("buffer");
                AsynchronousSocketChannel clientChannel = (AsynchronousSocketChannel) attachment.get("channel");
                buffer.flip();
                attachment.put("action", "write");

                // duplicate buffer
                String bufferContent = StandardCharsets.UTF_8.decode(buffer.duplicate()).toString();
                System.out.println(String.format("[%s] data read: %s", Thread.currentThread().getName(), bufferContent));


                attachment.put("buffer", buffer.duplicate());
                // write the buffer content back
                clientChannel.write(buffer, attachment, this);
                buffer.clear();

            } else if ("write".equals(action)) {
                AsynchronousSocketChannel clientChannel = (AsynchronousSocketChannel) attachment.get("channel");

                ByteBuffer written = (ByteBuffer) attachment.get("buffer");
                String bufferContent = StandardCharsets.UTF_8.decode(written.duplicate()).toString();
                System.out.println(String.format("[%s] data written: %s", Thread.currentThread().getName(), bufferContent));

                ByteBuffer buffer = ByteBuffer.allocate(32);
                attachment.put("action", "read");
                attachment.put("buffer", buffer);

                clientChannel.read(buffer, attachment, this);
            }
        }

        @Override
        public void failed(Throwable exc, Map<String, Object> attachment) {
            //
        }
    }

//    public void taowa() {
//        while (true) {
//            serverChannel.accept(
//                    null,
//                    new CompletionHandler<AsynchronousSocketChannel, Object>() {
//
//                        @Override
//                        public void completed(AsynchronousSocketChannel clientChannel, Object attachment) {
//                            if (serverChannel.isOpen()){
//                                serverChannel.accept(null, this);
//                            }
//
//                            if ((clientChannel != null) && (clientChannel.isOpen())) {
////                                ReadWriteHandler handler = new ReadWriteHandler();
//                                ByteBuffer buffer = ByteBuffer.allocate(32);
//
////                                Map<String, Object> readInfo = new HashMap<>();
////                                readInfo.put("action", "read");
////                                readInfo.put("buffer", buffer);
//
////                                clientChannel.read(buffer, readInfo, handler);
//
//                                clientChannel.read(
//                                        buffer,
//                                        null,
//                                        new CompletionHandler<Integer, Object>() {
//
//                                            @Override
//                                            public void completed(Integer result, Object attachment) {
//                                                clientChannel.write(
//                                                        buffer,
//                                                        null,
//                                                        new CompletionHandler<Integer, Object>() {
//                                                            @Override
//                                                            public void completed(Integer result, Object attachment) {
//
//                                                            }
//
//                                                            @Override
//                                                            public void failed(Throwable exc, Object attachment) {
//
//                                                            }
//                                                        }
//                                                );
//                                                buffer.clear();
//                                            }
//
//                                            @Override
//                                            public void failed(Throwable exc, Object attachment) {
//
//                                            }
//                                        }
//                                )
//                            }
//                        }
//                        @Override
//                        public void failed(Throwable exc, Object attachment) {
//                            // process error
//                        }
//                    });
//            System.in.read();
//        }
//    }
}
