/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package example.jmhbenchmark.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMHSample_11_Loops {

    /*
     * It would be tempting for users to do loops within the benchmarked method.
     * (This is the bad thing Caliper taught everyone). These tests explain why
     * this is a bad idea.
     *
     * Looping is done in the hope of minimizing the overhead of calling the
     * test method, by doing the operations inside the loop instead of inside
     * the method call. Don't buy this argument; you will see there is more
     * magic happening when we allow optimizers to merge the loop iterations.
     */

    /*
     * Suppose we want to measure how much it takes to sum two integers:
     */

    int x = 1;
    int y = 2;

    /*
     * This is what you do with JMH.
     */

    @Benchmark
    public int measureRight() {
        return (x + y);
    }

    /*
     * The following tests emulate the naive looping.
     * This is the Caliper-style benchmark.
     */
    private int reps(int reps) {
        int s = 0;
        for (int i = 0; i < reps; i++) {
            s += (x + y);
        }
        return s;
    }

    /*
     * We would like to measure this with different repetitions count.
     * Special annotation is used to get the individual operation cost.
     */

    @Benchmark
    @OperationsPerInvocation(1)
    public int measureWrong_1() {
        return reps(1);
    }

    @Benchmark
    @OperationsPerInvocation(10)
    public int measureWrong_10() {
        return reps(10);
    }

    @Benchmark
    @OperationsPerInvocation(100)
    public int measureWrong_100() {
        return reps(100);
    }

    @Benchmark
    @OperationsPerInvocation(1000)
    public int measureWrong_1000() {
        return reps(1000);
    }

    @Benchmark
    @OperationsPerInvocation(10000)
    public int measureWrong_10000() {
        return reps(10000);
    }

    @Benchmark
    @OperationsPerInvocation(100000)
    public int measureWrong_100000() {
        return reps(100000);
    }

    /*
     * ============================== HOW TO RUN THIS TEST: ====================================
     *
     * You might notice the larger the repetitions count, the lower the "perceived"
     * cost of the operation being measured. Up to the point we do each addition with 1/20 ns,
     * well beyond what hardware can actually do.
     *
     * This happens because the loop is heavily unrolled/pipelined, and the operation
     * to be measured is hoisted from the loop. Morale: don't overuse loops, rely on JMH
     * to get the measurement right.
     *
     * You can run this test:
     *
     * a) Via the command line:
     *    $ mvn clean install
     *    $ java -jar target/benchmarks.jar JMHSample_11 -f 1
     *    (we requested single fork; there are also other options, see -h)
     *
     * b) Via the Java API:
     *    (see the JMH homepage for possible caveats when running from IDE:
     *      http://openjdk.java.net/projects/code-tools/jmh/)
     */

    /**
     * {@link OperationsPerInvocation}告诉jmh，每次benchmark调用相当于“参数”次operation，
     * 所以benchmark在计算最终的ns/operation的时候，就再多除以“参数”，从而计算一次的量。
     *
     * 看运算结果，当100000的时候，平均每次的运算时间远小于了1/20ns = 0.05ns，已经远超硬件的能力范畴了。
     * 所以说，jvm一定是在这些循环中加入优化了，否则不可能这么快的。
     *
     * 结论：如果想看100次调用的事件，不要使用循环100次进行“卡尺式”测试，使用jmh进行benchmark就行了。
     *
         Benchmark                               Mode  Cnt  Score   Error  Units
         JMHSample_11_Loops.measureRight         avgt    5  2.239 ± 0.465  ns/op
         JMHSample_11_Loops.measureWrong_1       avgt    5  2.379 ± 0.408  ns/op
         JMHSample_11_Loops.measureWrong_10      avgt    5  0.261 ± 0.041  ns/op
         JMHSample_11_Loops.measureWrong_100     avgt    5  0.027 ± 0.001  ns/op
         JMHSample_11_Loops.measureWrong_1000    avgt    5  0.033 ± 0.001  ns/op
         JMHSample_11_Loops.measureWrong_10000   avgt    5  0.020 ± 0.002  ns/op
         JMHSample_11_Loops.measureWrong_100000  avgt    5  0.018 ± 0.001  ns/op
     * @param args
     * @throws RunnerException
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHSample_11_Loops.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

}
