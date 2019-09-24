package example.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
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

        ByteBuffer buffer = ByteBuffer.allocate(2014);

        while (true) {
            // clear. ready to read: position = 0, limit = capacity
            buffer.clear();
            int r = fcin.read(buffer);

            if (r == -1) {
                break;
            }

            // flip. ready to write: limit = position, position = 0
            buffer.flip();
            fcout.write(buffer);
        }
    }
}
