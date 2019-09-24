package example.nio.asyncio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * ServerSocketChannel只能在Selector上注册OP_ACCEPT事件；
 * SocketChannel则可以注册OP_READ和OP_WRITE等
 *
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
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(port));
            // 告诉selector，我们对OP_ACCEPT事件感兴趣（这是适用于ServerSocketChannel的唯一事件类型）
            // SelectionKey的作用就是，当事件发生时，selector提供对应于那个事件的SelectionKey
            SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

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

                // 是有新连接了
                if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                    // Accept the new connection
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    // 不用担心accept()方法会阻塞，因为已经确定这个channel（这个端口）上是有一个新连接了
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);

                    // 我们期望从这个socket上读取数据，所以也注册到selector，等通知再来读。这次注册的是OP_READ：“可读”就通知我
                    // Add the new connection to the selector
                    SelectionKey newKey = sc.register(selector, SelectionKey.OP_READ);

                    System.out.println("Got connection from " + sc);

                    // 是socket上有可读的数据来了
                } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                    // TODO: infinite read request, why?
                    // Read the data
                    SocketChannel sc = (SocketChannel) key.channel();

                    // Echo data
                    int bytesEchoed = 0;
                    while (true) {
                        echoBuffer.clear();

                        int r = sc.read(echoBuffer);

                        if (r <= 0) {
                            break;
                        }

                        echoBuffer.flip();

                        sc.write(echoBuffer);
                        bytesEchoed += r;
                    }

                    System.out.println("Echoed " + bytesEchoed + " from " + sc);
                }
                // 处理过了，就删了，这个set一直是动态增长/减少的
                it.remove();
            }

//            System.out.println( "going to clear" );
//            selectedKeys.clear();
//            System.out.println( "cleared" );
        }
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
