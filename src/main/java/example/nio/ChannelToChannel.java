package example.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Channel -> Channel
 */
public class ChannelToChannel {

    public static void main(String args[]) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java ChannelToChannel infile outfile");
            System.exit(1);
        }

        String infile = args[0];
        String outfile = args[1];

        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);

        FileChannel fcin = fin.getChannel();
        FileChannel fcout = fout.getChannel();

        // 如果两个通道有一个是FileChannel，则可以直接通道到通道；
        // 此外要注意，在SoketChannel的实现中，SocketChannel只会传输此刻准备好的数据（可能不足count字节）。
        // 因此，SocketChannel可能不会将请求的所有数据(count个字节)全部传输到FileChannel中。
        // 另外，这个可以使用零拷贝，所以更高效
//        fcin.transferTo(0, fcin.size(), fcout);

        ByteBuffer buffer = ByteBuffer.allocate(2014);

//        while (true) {
//            buffer.clear();
//            int r = fcin.read(buffer);
//
//            if (r == -1) {
//                break;
//            }
//
//            buffer.flip();
//            fcout.write(buffer);
//        }

        while(fcin.read(buffer) > 0) {
            // flip. ready to write: limit = position, position = 0
            buffer.flip();
            fcout.write(buffer);
            // clear. ready to read: position = 0, limit = capacity
            buffer.clear();
        }
        fcin.close();
        fcout.close();
    }
}
