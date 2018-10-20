package example.jmhbenchmark;

import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * {@link Setup}和{@link TearDown}必须用在{@link State}标识的类内部。
 *
 * @author liuhaibo on 2018/06/06
 */
@State(Scope.Thread)
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.SECONDS)
public class StreamBenchMark {

    private List<Integer> list = new ArrayList<>();
    int counter = 0;

    @Setup(Level.Invocation)
    public void init() throws InterruptedException {
        for (int i = 0; i < 10000000; i++) {
            int value = RandomUtils.nextInt(0, Integer.MAX_VALUE);
            list.add(value);
        }
        System.out.println("初始化了一次： " + ++counter);
//        TimeUnit.MILLISECONDS.sleep(10);
    }

//    @Benchmark
    public void iteratorDelete() {
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() >= Integer.MAX_VALUE / 2) {
                iterator.remove();
            }
        }
    }

//    @Benchmark
    public void lambdaDelete() {
        list.removeIf(integer -> integer >= Integer.MAX_VALUE / 2);
    }

    @Benchmark
    public void streamDelete() {
        list = list.stream().filter(integer -> integer < Integer.MAX_VALUE / 2).collect(Collectors.toList());
    }

    /**
     * 第一部分是不带<code>TimeUnit.MILLISECONDS.sleep(10)</code>的，第二部分带。
     *
     * （从现象来看，一次iteration 10s，如果10s过了还没调用完一次benchmark，就等调用完之后，iteration再结束。）
     *
     * For 10,000,000 items in {@link #list}
         Benchmark                              Mode  Cnt  Score    Error  Units
         StreamBenchMark.streamDelete  avgt    5  6.789 ± 20.928   s/op

     |只有stream才出结果。iterator太慢了。lamdba的removeIf由于是并行运算，oom了。

         parallelStream:
         Benchmark                              Mode  Cnt   Score    Error  Units
         StreamBenchMark.streamDelete  avgt    5  10.432 ± 18.657   s/op


     * For 1,000,000 items in {@link #list}
         Benchmark                                 Mode  Cnt   Score   Error  Units
         StreamBenchMark.iteratorDelete   avgt    5  29.264 ± 1.792   s/op
         StreamBenchMark.lambdaDelete     avgt    5   1.015 ± 2.917   s/op
         StreamBenchMark.streamDelete     avgt    5   1.480 ± 2.732   s/op

         Benchmark                                 Mode  Cnt   Score   Error  Units
         StreamBenchMark.iteratorDelete   avgt    5  27.155 ± 1.462   s/op
         StreamBenchMark.lambdaDelete     avgt    5   0.961 ± 2.534   s/op
         StreamBenchMark.streamDelete     avgt    5   1.684 ± 2.180   s/op

     * For 100,000 items in {@link #list}
         Benchmark                                Mode  Cnt  Score   Error  Units
         StreamBenchMark.iteratorDelete  avgt    5  0.262 ± 0.034   s/op
         StreamBenchMark.lambdaDelete    avgt    5  0.239 ± 0.346   s/op
         StreamBenchMark.streamDelete    avgt    5  0.420 ± 0.372   s/op

         Benchmark                                Mode  Cnt  Score   Error  Units
         StreamBenchMark.iteratorDelete  avgt    5  0.268 ± 0.110   s/op
         StreamBenchMark.lambdaDelete    avgt    5  0.245 ± 0.434   s/op
         StreamBenchMark.streamDelete    avgt    5  0.577 ± 1.242   s/op

     * For 10,000 items in {@link #list}
         不加10ms sleep，对结果影响不是很大，毕竟benchmark已经脱离1ms量级了：
         Benchmark                                 Mode  Cnt   Score    Error   Units
         StreamBenchMark.iteratorDelete   avgt    5  34.231 ± 13.302   ms/op
         StreamBenchMark.lambdaDelete     avgt    5  63.468 ± 28.472   ms/op
         StreamBenchMark.streamDelete     avgt    5  94.354 ± 82.852   ms/op

         Benchmark                                 Mode  Cnt   Score    Error   Units
         StreamBenchMark.iteratorDelete   avgt    5  27.194 ± 10.603   ms/op
         StreamBenchMark.lambdaDelete     avgt    5  65.184 ± 39.559   ms/op
         StreamBenchMark.streamDelete     avgt    5  93.825 ± 31.696   ms/op

     * For 100 items in {@link #list}
         不那么准确，还是受{@link Level#Invocation}影响较大的：
         Benchmark                                 Mode  Cnt  Score   Error   Units
         StreamBenchMark.iteratorDelete   avgt    5  3.479 ± 1.205   ms/op
         StreamBenchMark.lambdaDelete     avgt    5  7.402 ± 2.985   ms/op
         StreamBenchMark.streamDelete     avgt    5  8.625 ± 2.635   ms/op

         更准确些：
         Benchmark                                 Mode  Cnt  Score   Error   Units
         StreamBenchMark.iteratorDelete   avgt    5  1.865 ± 1.325   ms/op
         StreamBenchMark.lambdaDelete     avgt    5  4.660 ± 2.280   ms/op
         StreamBenchMark.streamDelete     avgt    5  5.913 ± 3.116   ms/op

     * For 10 items in {@link #list}
         本身benchmark在1ms上下，如果不让payload多运行一会儿，
         使用{@link Level#Invocation}会给系统造成太大负担，导致结果不准确：
         Benchmark                                 Mode  Cnt  Score   Error   Units
         StreamBenchMark.iteratorDelete   avgt    5  1.393 ± 0.949   ms/op
         StreamBenchMark.lambdaDelete     avgt    5  2.547 ± 0.666   ms/op
         StreamBenchMark.streamDelete     avgt    5  3.023 ± 1.235   ms/op

         这就准确多了：
         Benchmark                                 Mode  Cnt  Score   Error   Units
         StreamBenchMark.iteratorDelete   avgt    5  0.159 ± 0.175   ms/op
         StreamBenchMark.lambdaDelete     avgt    5  0.990 ± 0.845   ms/op
         StreamBenchMark.streamDelete     avgt    5  0.763 ± 0.900   ms/op
     *
     * Exclusion: lambda deletion and iteration deletion are much better than stream deletion if collections is small.
     * 随着集合的变大，倍数差越来越小。10:5倍；100:3倍；10000：3倍；十万：1.5倍；一百万：1/20倍，显然用stream和lambda更快了。
     * 一千万？iterator就别想了，慢到死……
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StreamBenchMark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
