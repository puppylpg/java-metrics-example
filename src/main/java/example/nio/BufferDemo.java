package example.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class BufferDemo {

    static public void main(String args[]) {
        // allocate
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // put
        buffer.put((byte) 'a');
        buffer.put((byte) 'b');
        buffer.put((byte) 'c');

        buffer.flip();

        // get
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());


        // float buffer
        FloatBuffer fBuffer = FloatBuffer.allocate(10);
        for (int i = 0; i < fBuffer.capacity(); ++i) {
            float f = (float) Math.sin((((float) i) / 10) * (2 * Math.PI));
            fBuffer.put(f);
        }

        fBuffer.flip();

        while (fBuffer.hasRemaining()) {
            float f = fBuffer.get();
            System.out.println(f);
        }


        // specify array
        byte array[] = new byte[1024];

        buffer = ByteBuffer.wrap(array);

        buffer.put((byte) 'a');
        buffer.put((byte) 'b');
        buffer.put((byte) 'c');

        buffer.flip();

        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());


        // putXXX
        buffer.putInt(30);
        buffer.putLong(7000000000000L);
        buffer.putDouble(Math.PI);

        buffer.flip();

        // getXXX
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getDouble());


        // slice to sub-buffer
        buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }

        buffer.position(3);
        buffer.limit(7);

        ByteBuffer slice = buffer.slice();

        for (int i = 0; i < slice.capacity(); ++i) {
            byte b = slice.get(i);
            b *= 11;
            slice.put(i, b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (buffer.remaining() > 0) {
            System.out.println(buffer.get());
        }

//        buffer.rewind();
//        buffer.compact();
        // the following two are used together
//        buffer.mark();
//        buffer.reset();
    }

    public static ByteBuffer normalBuffer() {
        return ByteBuffer.allocate(1024);
    }

    // 直接缓冲区，与底层实现相关
    public static ByteBuffer directBuffer() {
        return ByteBuffer.allocateDirect(1024);
    }

    // 内存映射文件：现代os“将文件的部分映射为内存的部分，从而实现文件系统”。Java提供了对该机制的访问。
    // 这并不是简单地把文件读入内存，但用起来差不多是一个意思。另外，主要write数组，文件就直接被改了。
    public static ByteBuffer memoryMappingFile(FileChannel fileChannel) throws IOException {
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
        return mappedByteBuffer;
    }
}
