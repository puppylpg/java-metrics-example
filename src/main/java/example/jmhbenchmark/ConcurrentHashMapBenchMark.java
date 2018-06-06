package example.jmhbenchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author liuhaibo on 2018/06/06
 */
public class ConcurrentHashMapBenchMark {

    @Benchmark
    public void forHashtable() throws InterruptedException {
        getPut100K4Threads(new ThreadState().hashtable);
    }

    @Benchmark
    public void forSynchronizedHashMap() throws InterruptedException {
        getPut100K4Threads(new ThreadState().synchronizedHashMap);
    }

    @Benchmark
    public void forConcurrentHashMap() throws InterruptedException {
        getPut100K4Threads(new ThreadState().concurrentHashMap);
    }


    private void getPut100K4Threads(Map<String, Object> map) throws InterruptedException {
        ExecutorService executorService =
                Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 100_000; j++) {
                    int value = ThreadLocalRandom
                            .current()
                            .nextInt(10000);
                    String key = String.valueOf(value);
                    map.put(key, value);
                    map.get(key);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    /**
     * Benchmark                                                        Mode  Cnt   Score   Error  Units
     * jmhBenchMark.ConcurrentHashMapBenchMark.forConcurrentHashMap    thrpt    5  71.604 ± 2.757  ops/s
     * jmhBenchMark.ConcurrentHashMapBenchMark.forHashtable            thrpt    5   9.484 ± 1.274  ops/s
     * jmhBenchMark.ConcurrentHashMapBenchMark.forSynchronizedHashMap  thrpt    5   9.530 ± 1.446  ops/s
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ConcurrentHashMapBenchMark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Thread)
    public static class ThreadState {
        volatile Map<String, Object> hashtable = new Hashtable<>();
        volatile Map<String, Object> synchronizedHashMap =
                Collections.synchronizedMap(new HashMap<>());
        volatile Map<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
    }
}
