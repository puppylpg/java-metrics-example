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

        ByteBuffer buffer = ByteBuffer.allocate(10);

        // 读完文件
        while (fc.read(buffer) > 0) {
            buffer.flip();

            int i = 0;
            // 读完buffer
            while (buffer.hasRemaining()) {
                byte b = buffer.get();
                System.out.println("Character " + i + ": " + ((char) b));
                i++;
            }
            buffer.clear();
        }

        fc.close();
        fin.close();
    }

    public static void writeFile() throws IOException {
        byte message[] = {83, 111, 109, 101, 32, 98, 121, 116, 101, 115, 46};

        FileOutputStream fout = new FileOutputStream("writesomebytes.txt");

        FileChannel fc = fout.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        for (byte aMessage : message) {
            buffer.put(aMessage);
        }

        buffer.flip();

        // 读完buffer
        while(buffer.hasRemaining()) {
            fc.write(buffer);
        }

        fc.close();
        fout.close();
    }
}
