package example.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelBufferDemo {

    public static void main(String args[]) throws Exception {
        readFile();
        writeFile();
    }

    public static void readFile() throws IOException {
        FileInputStream fin = new FileInputStream(".gitignore");
        FileChannel fc = fin.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        fc.read(buffer);

        buffer.flip();

        int i = 0;
        while (buffer.remaining() > 0) {
            byte b = buffer.get();
            System.out.println("Character " + i + ": " + ((char) b));
            i++;
        }

        fin.close();
    }

    public static void writeFile() throws IOException {
        byte message[] = {83, 111, 109, 101, 32, 98, 121, 116, 101, 115, 46};

        FileOutputStream fout = new FileOutputStream("writesomebytes.txt");

        FileChannel fc = fout.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        for (int i = 0; i < message.length; ++i) {
            buffer.put(message[i]);
        }

        buffer.flip();

        fc.write(buffer);

        fout.close();
    }
}
