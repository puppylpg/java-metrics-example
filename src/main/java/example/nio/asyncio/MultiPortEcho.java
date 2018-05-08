package example.nio.asyncio;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class MultiPortEcho {
    private int ports[];
    private ByteBuffer echoBuffer = ByteBuffer.allocate(1024);

    public MultiPortEcho(int ports[]) throws IOException {
        this.ports = ports;

        go();
    }

    private void go() throws IOException {
        // Create a new selector
        Selector selector = Selector.open();

        // Open a listener on each port, and register each one with the selector
        for (int port : ports) {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ServerSocket ss = ssc.socket();
            InetSocketAddress address = new InetSocketAddress(port);
            ss.bind(address);

            SelectionKey key = ssc.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Going to listen on " + port);
        }

        while (true) {
            int num = selector.select();

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator it = selectedKeys.iterator();

            while (it.hasNext()) {
                SelectionKey key = (SelectionKey) it.next();

                if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                    // Accept the new connection
                    handleConnect(selector, key);
                    it.remove();

                } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                    // Read the data
                    handleSocket(key);
                    it.remove();
                }

            }

        }
    }

    private void handleConnect(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        System.out.println("Got connection from " + sc);

        // Add the new connection to the selector
        SelectionKey clientKey = sc.register(selector, SelectionKey.OP_READ);
        System.out.println("Register client channel " + clientKey.channel() + " to selector.");
    }

    private void handleSocket(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        // echo data
        reply(sc);
    }

    private void reply(SocketChannel sc) throws IOException {
        int bytesEchoed = 0;
        while (true) {
            echoBuffer.clear();
            int i = sc.read(echoBuffer);
            if (i < 0) {
                break;
            }
            echoBuffer.flip();
            sc.write(echoBuffer);
            bytesEchoed += i;
        }
        System.out.println("Echoed " + bytesEchoed + " from " + sc);
    }

    public static void main(String args[]) throws Exception {
        if (args.length <= 0) {
            System.err.println("Usage: java MultiPortEcho port [port port ...]");
            System.exit(1);
        }

        int ports[] = new int[args.length];

        for (int i = 0; i < args.length; ++i) {
            ports[i] = Integer.parseInt(args[i]);
        }

        new MultiPortEcho(ports);
    }
}
