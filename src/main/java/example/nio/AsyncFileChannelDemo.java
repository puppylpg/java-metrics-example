package example.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

/**
 * FileChannel只能是阻塞的。所以Java7引入了{@link java.nio.channels.AsynchronousFileChannel}。
 *
 * @author liuhaibo on 2019/09/26
 */
public class AsyncFileChannelDemo {

    public static void main(String... args) throws IOException, InterruptedException {
        asyncRead();
        asyncWrite();

        System.out.println("Sleep to wait async io..");
        Thread.sleep(1000);
    }

    private static void asyncRead() throws IOException {
        // 使用相对路径读。Note：换目录就崩
        Path gitIgnore = Paths.get(".gitignore");
        OpenOption[] options = {StandardOpenOption.READ};
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(gitIgnore, options);

        ByteBuffer byteBuffer = ByteBuffer.allocate(5);

        // 方法一：使用Future去读
        // read once
        Future<Integer> future = fileChannel.read(byteBuffer, 0);

        // reture read size
//        System.out.println(future.get());

        while (!future.isDone()) {
            // wait
        }

        System.out.println("Read down by Future.");
        output(byteBuffer);

        // 方法二：使用CompletionHandle去读
        byteBuffer.clear();
        fileChannel.read(byteBuffer, 0, null, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("Read result code: " + result + ". I have nothing to do with the ATTACHMENT object.");
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {

            }
        });
        // 注意这个是在sleep的提示语后输出的
        output(byteBuffer);
    }

    private static void asyncWrite() throws IOException {
        Path forWrite = Paths.get("forwrite.txt");
        OpenOption[] options = {StandardOpenOption.WRITE, StandardOpenOption.CREATE};
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(forWrite, options);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        buffer.put("test data".getBytes());
        buffer.flip();

        // 方法一：使用Future去写
        Future<Integer> operation = fileChannel.write(buffer, 0);

        while(!operation.isDone()) {
            // wait
        }

        System.out.println("Write done by Future.");

        fileChannel.write(buffer, 0, null, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("Write result code: " + result + ". I have nothing to do with the ATTACHMENT object.");
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {

            }
        });
    }

    private static void output(ByteBuffer byteBuffer) {
        byteBuffer.flip();
        byte[] data = new byte[byteBuffer.limit()];
        byteBuffer.get(data);
        System.out.println(new String(data));
    }
}
