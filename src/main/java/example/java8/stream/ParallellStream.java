package example.java8.stream;

import org.apache.commons.lang3.RandomUtils;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * n次掷骰子操作，每次掷两次求和。
 *
 * 结果：使用并行流确实能让所有核都跑起来，但是要远远慢于非并行流……
 * 结论：数据太简单了，分解去计算，最后再合并的开销远大于直接计算这个数的开销……
 * @author liuhaibo on 2018/03/12
 */
public class ParallellStream {

    public static void main(String... args) {
        int n = 1000 * 1000 * 10;
        double fraction = 1.0 / n;
        System.out.println("=== start ===");
        long t1 = System.currentTimeMillis();
        // mapToObj produces a Stream, which can use simple collect();
        // or it'll produces a IntStream, which only has a complex collect()
        System.out.println(
                IntStream.range(0, n)
                        .parallel()
                        .mapToObj(i -> RandomUtils.nextInt(1, 7) + RandomUtils.nextInt(1, 7))
                        .collect(Collectors.groupingBy(num -> num, Collectors.summingDouble(i -> fraction)))
        );
        long t2 = System.currentTimeMillis();
        System.out.println("=== end ===");
        System.out.println(String.format("Time used: [%d] ms.", (t2 - t1)));
    }
}
