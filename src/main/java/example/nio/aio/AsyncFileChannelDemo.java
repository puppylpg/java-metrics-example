package example.nio.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
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
        asyncWrite();
        asyncRead();

        System.out.println(String.format("[%s] Sleep to wait async io...", Thread.currentThread().getName()));
        Thread.sleep(1000);
        System.out.println(String.format("[%s] Main thread exit...", Thread.currentThread().getName()));
    }

    private static void asyncRead() throws IOException {
        // 使用相对路径读。Note：换目录就崩
        Path gitIgnore = Paths.get("forwrite.txt");
        OpenOption[] options = {StandardOpenOption.READ};
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(gitIgnore, options);

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        // 方法一：使用Future去读
        // read once
        Future<Integer> future = fileChannel.read(byteBuffer, 0);

        // reture read size
//        System.out.println(future.get());

        while (!future.isDone()) {
            // wait
        }

        System.out.println(String.format("[%s] Read done by Future.", Thread.currentThread().getName()));
        output(byteBuffer);
        byteBuffer.clear();

        // 方法二：使用CompletionHandle去读
        fileChannel.read(byteBuffer, 0, "A Read Attachment", new CompletionHandler<Integer, String>() {
            @Override
            public void completed(Integer result, String attachment) {
                System.out.println(String.format("[%s] Read result code: %s. This is the attachment object I put when reading: %s.", Thread.currentThread().getName(), result, attachment));
                output(byteBuffer);
            }

            @Override
            public void failed(Throwable exc, String attachment) {
                System.out.println(String.format("[%s] Read failed.", Thread.currentThread().getName()));
            }
        });
    }

    private static void asyncWrite() throws IOException {
        // ${working directory}/forwrite.txt
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

        System.out.println(String.format("[%s] Write done by Future.", Thread.currentThread().getName()));

        fileChannel.write(buffer, 0, "A Write Attachment", new CompletionHandler<Integer, String>() {
            @Override
            public void completed(Integer result, String attachment) {
                System.out.println(String.format("[%s] Write result code: %s. This is the attachment object I put when writing: %s.", Thread.currentThread().getName(), result, attachment));
                output(buffer);
            }

            @Override
            public void failed(Throwable exc, String attachment) {
                System.out.println(String.format("[%s] Write failed.", Thread.currentThread().getName()));
            }
        });
    }

    private static void output(ByteBuffer byteBuffer) {
        byteBuffer.flip();
        byte[] data = new byte[byteBuffer.limit()];
        byteBuffer.get(data);
        System.out.println(String.format("[%s] %s", Thread.currentThread().getName(), new String(data, StandardCharsets.UTF_8)));
    }
}
