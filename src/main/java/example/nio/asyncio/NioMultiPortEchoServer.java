package example.nio.asyncio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * ServerSocketChannel只能在Selector上注册OP_ACCEPT事件；
 * SocketChannel则可以注册OP_READ和OP_WRITE等
 * <p>
 * 这个程序可以仅使用一个线程，因为它只是一个演示，但是在现实场景中，创建一个线程池来负责 I/O 事件处理中的耗时部分会更有意义。
 */
public class NioMultiPortEchoServer {
    private int ports[];
    private ByteBuffer echoBuffer = ByteBuffer.allocate(1024);

    public NioMultiPortEchoServer(int ports[]) throws IOException {
        this.ports = ports;

        go();
    }

    private void go() throws IOException {
        // Create a new selector
        Selector selector = Selector.open();

        // Open a listener on each port, and register each one
        // with the selector
        for (int port : ports) {
            // 获得channel，设为非阻塞
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            // channel绑定到相应的端口
            serverSocketChannel.socket().bind(new InetSocketAddress(port));

            registerServerSocket(serverSocketChannel, selector);

            System.out.println("Going to listen on " + port);
        }

        while (true) {
            // 这个方法会阻塞（如果没事儿干，你就歇着吧），直到至少有一个已注册的事件发生。
            // 当一个或者更多的事件发生时，select()方法将返回所发生的事件的数量
            int num = selector.select();

            // 发生了事件的 SelectionKey 对象的一个 集合
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();

            // 依次判断每个事件发生的到底是啥事儿
            while (it.hasNext()) {
                SelectionKey key = it.next();

                // 处理过了，就删了，防止一会儿重复处理。（可认为Selector只往set里加，但是不删！！！）
                it.remove();

                // SelectionKey.channel()方法返回的通道需要转型成你要处理的类型，如ServerSocketChannel或SocketChannel等。
                // 是有新连接了
                if (key.isAcceptable()) {
                    acceptSocketAndRegisterIt(key, selector);

                    // 是socket上有可读的数据来了
                } else if (key.isReadable()) {
                    readSocket(key);
                }
            }
            // 如果上面没有一个一个删掉，这里直接清空也行
//            selectedKeys.clear();
        }
    }

    private void registerServerSocket(ServerSocketChannel serverSocketChannel, Selector selector) throws ClosedChannelException {
        // 告诉selector，我们对OP_ACCEPT事件感兴趣（这是适用于ServerSocketChannel的唯一事件类型）
        // SelectionKey的作用就是，当事件发生时，selector提供对应于那个事件的SelectionKey
        // 这里，ServerSocketChannel所支持的操作只有SelectionKey.OP_ACCEPT
        SelectionKey key = serverSocketChannel.register(selector, serverSocketChannel.validOps());
    }

    private void acceptSocketAndRegisterIt(SelectionKey key, Selector selector) throws IOException {
        // Accept the new connection
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        // 不用担心accept()方法会阻塞，因为已经确定这个channel（这个端口）上是有一个新连接了
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        // 我们期望从这个socket上读取数据，所以也注册到selector，等通知再来读。这次注册的是OP_READ：“可读”就通知我
        // Add the new connection to the selector
        SelectionKey newKey = socketChannel.register(selector, SelectionKey.OP_READ);

        System.out.println("+++ New connection: " + socketChannel);
    }

    private void readSocket(SelectionKey key) throws IOException {
        // Read the data
        SocketChannel socketChannel = (SocketChannel) key.channel();

        // Echo data
        int bytesEchoed = 0, r = 0;
        while ((r = socketChannel.read(echoBuffer)) > 0) {
            // flip. ready to write: limit = position, position = 0
            echoBuffer.flip();
            socketChannel.write(echoBuffer);
            bytesEchoed += r;
            // clear. ready to read: position = 0, limit = capacity
            echoBuffer.clear();
        }
        System.out.println("Echoed " + bytesEchoed + " from " + socketChannel);
        // CLOSE SOCKET AFTER HANDLED
        socketChannel.close();
        System.out.println("--- Close connection: " + socketChannel);
    }

    static public void main(String args[]) throws Exception {
        if (args.length <= 0) {
            System.err.println("Usage: java NioMultiPortEchoServer port [port port ...]");
            System.exit(1);
        }

        int ports[] = new int[args.length];

        for (int i = 0; i < args.length; ++i) {
            ports[i] = Integer.parseInt(args[i]);
        }

        new NioMultiPortEchoServer(ports);
    }
}
